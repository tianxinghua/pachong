package com.shangpin.ephub.product.business.rest.price.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierPriceChangeRecordDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
import com.shangpin.ephub.client.message.price.body.ProductPriceDTO;
import com.shangpin.ephub.client.product.business.price.dto.PriceDto;

@Service
public class PriceService {
	
	@Autowired
	private HubSupplierSpuGateWay hubSupplierSpuGateWay;
	@Autowired 
	private HubSupplierSkuGateWay hubSupplierSkuGateWay;
	
	public void savePriceRecordAndSendConsumer(PriceDto priceDto) throws Exception{
		HubSupplierSpuDto supplierSpuDto = priceDto.getHubSpu();
		List<HubSupplierSkuDto> supplierSkus = priceDto.getHubSkus();
		String supplierNo = priceDto.getSupplierNo();
		
		HubSupplierSpuDto spuDtoSel = isSupplierSeasonNameChanged(supplierSpuDto);
		if(null != spuDtoSel){
			List<HubSupplierSkuDto> hubSkus = findSupplierSkus(spuDtoSel.getSupplierSpuId());
			if(CollectionUtils.isNotEmpty(hubSkus)){
				for(HubSupplierSkuDto skuDto : hubSkus){
					if(!StringUtils.isEmpty(skuDto.getSpSkuNo())){
						HubSupplierPriceChangeRecordDto recordDto = new HubSupplierPriceChangeRecordDto();
						convertPriceDtoToRecordDto(supplierNo,supplierSpuDto,skuDto,recordDto);
						saveHubSupplierPriceChangeRecordDto(recordDto);
						ProductPriceDTO retryPrice  = new ProductPriceDTO();
						convertPriceDtoToRetryPrice(supplierNo,supplierSpuDto,skuDto,retryPrice);				
						//TODO 发送消息
					}
				}
			}
		}
		
		for(HubSupplierSkuDto skuDto : supplierSkus){
			boolean isChanged = isPriceChanged(skuDto);
			if(isChanged){
				HubSupplierPriceChangeRecordDto recordDto = new HubSupplierPriceChangeRecordDto();
				convertPriceDtoToRecordDto(supplierNo,supplierSpuDto,skuDto,recordDto);
				saveHubSupplierPriceChangeRecordDto(recordDto);
				ProductPriceDTO retryPrice  = new ProductPriceDTO();
				convertPriceDtoToRetryPrice(supplierNo,supplierSpuDto,skuDto,retryPrice);
				//TODO 发送消息
			}
		}
	}
	/**
	 * 将供应商原始表的信息转换为供价记录消息体
	 * @param supplierNo 供应商门户编号
	 * @param supplierSpuDto 供应商原始spu表对象
	 * @param supplierSkuDto 供应商原始sku表对象
	 * @param retryPrice 供价记录消息体
	 */
	public void convertPriceDtoToRetryPrice(String supplierNo,HubSupplierSpuDto supplierSpuDto,HubSupplierSkuDto supplierSkuDto,ProductPriceDTO retryPrice){
		retryPrice.setSopUserNo(supplierSkuDto.getSupplierId());
		retryPrice.setSkuNo(supplierSkuDto.getSpSkuNo());
		retryPrice.setSupplierSkuNo(supplierSkuDto.getSupplierSkuNo());
		BigDecimal marketPrice = supplierSkuDto.getMarketPrice();
		retryPrice.setMarketPrice(null != marketPrice ? marketPrice.toString() : ""); 
		BigDecimal supplyPrice = supplierSkuDto.getSupplyPrice();
		retryPrice.setPurchasePrice(null != supplyPrice ? supplyPrice.toString() : "");
		retryPrice.setMarketSeason(supplierSpuDto.getSupplierSeasonname());
		retryPrice.setCurrency(StringUtils.isEmpty(supplierSkuDto.getMarketPriceCurrencyorg()) ? supplierSkuDto.getSupplyPriceCurrency() : supplierSkuDto.getMarketPriceCurrencyorg());
	}
	/**
	 * 将供应商原始表的信息转换为供价记录表实体类
	 * @param supplierNo 供应商门户编号
	 * @param supplierSpuDto 供应商原始spu表对象
	 * @param supplierSkuDto 供应商原始sku表对象
	 * @param recordDto 供价记录表实体类
	 */
	public void convertPriceDtoToRecordDto(String supplierNo,HubSupplierSpuDto supplierSpuDto,HubSupplierSkuDto supplierSkuDto,HubSupplierPriceChangeRecordDto recordDto){
		recordDto.setSupplierId(supplierSkuDto.getSupplierId());
		recordDto.setSupplierNo(supplierNo);
		recordDto.setSupplierSkuNo(supplierSkuDto.getSupplierSkuNo());
		recordDto.setSupplierSpuNo(supplierSpuDto.getSupplierSpuNo());
		recordDto.setSpSkuNo(supplierSkuDto.getSpSkuNo());
		recordDto.setMarketPrice(supplierSkuDto.getMarketPrice());
		recordDto.setSupplyPrice(supplierSkuDto.getSupplyPrice());
		recordDto.setCurrency(StringUtils.isEmpty(supplierSkuDto.getMarketPriceCurrencyorg()) ? supplierSkuDto.getSupplyPriceCurrency() : supplierSkuDto.getMarketPriceCurrencyorg()); 
		recordDto.setMarketSeason(supplierSpuDto.getSupplierSeasonname()); 
//		recordDto.setState(false);
		recordDto.setCreateTime(new Date());
	}

	/**
	 * 查询季节是否发生变化
	 * @param hubSpu
	 * @return
	 * @throws Exception
	 */
	public HubSupplierSpuDto isSupplierSeasonNameChanged(HubSupplierSpuDto supplierSpuDto) throws Exception {
		HubSupplierSpuCriteriaDto criteriaDto = new HubSupplierSpuCriteriaDto();
		criteriaDto.setFields("supplier_spu_id,supplier_seasonname");
		criteriaDto.createCriteria().andSupplierIdEqualTo(supplierSpuDto.getSupplierId()).andSupplierSpuNoEqualTo(supplierSpuDto.getSupplierSpuNo());
		List<HubSupplierSpuDto> hubSpus = hubSupplierSpuGateWay.selectByCriteria(criteriaDto);
		if(null == hubSpus || hubSpus.size() == 0){
			return null;
		}else{
			if(supplierSpuDto.getSupplierSeasonname().equals(hubSpus.get(0).getSupplierSeasonname())){
				return null;
			}else{
				return hubSpus.get(0);
			}
		}
	} 
	/**
	 * 查询价格是否变化
	 * @param hubSku
	 * @return
	 */
	public boolean isPriceChanged(HubSupplierSkuDto hubSku) throws Exception{
		HubSupplierSkuCriteriaDto criteriaDto = new HubSupplierSkuCriteriaDto();
		criteriaDto.setFields("supply_price,market_price,sp_sku_no");
		criteriaDto.createCriteria().andSupplierIdEqualTo(hubSku.getSupplierId()).andSupplierSkuNoEqualTo(hubSku.getSupplierSkuNo());
		List<HubSupplierSkuDto> hubSkus = hubSupplierSkuGateWay.selectByCriteria(criteriaDto);
		if(CollectionUtils.isNotEmpty(hubSkus)){
			HubSupplierSkuDto hubSkuSel = hubSkus.get(0);
			hubSku.setSpSkuNo(hubSkuSel.getSpSkuNo()); 
			BigDecimal supplierPrice = null;
			if(hubSku.getSupplyPrice()!=null){
				supplierPrice = hubSku.getSupplyPrice().setScale(2,BigDecimal.ROUND_HALF_UP); 
				if(!supplierPrice.equals(hubSkuSel.getSupplyPrice())){
					return true;
				}
			}
			BigDecimal marketPrice = null;
			if(hubSku.getMarketPrice()!=null){
				marketPrice =  hubSku.getMarketPrice().setScale(2,BigDecimal.ROUND_HALF_UP);
				if(!marketPrice.equals(hubSkuSel.getMarketPrice())){
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 根据supplierSpuId查找该spu下的所有sku
	 * @param supplierSpuId
	 * @return
	 * @throws Exception
	 */
	public List<HubSupplierSkuDto> findSupplierSkus(Long supplierSpuId) throws Exception{
		HubSupplierSkuCriteriaDto criteriaDto = new HubSupplierSkuCriteriaDto();
		criteriaDto.createCriteria().andSupplierSpuIdEqualTo(supplierSpuId);
		return hubSupplierSkuGateWay.selectByCriteria(criteriaDto);
	}
	
	public Long saveHubSupplierPriceChangeRecordDto(HubSupplierPriceChangeRecordDto recordDto){
		return 1L;
	}
	
}
