package com.shangpin.ephub.product.business.rest.price.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.enumeration.State;
import com.shangpin.ephub.client.data.mysql.enumeration.Type;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.season.gateway.HubSeasonDicGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierPriceChangeRecordCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierPriceChangeRecordDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierPriceChangeRecordGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.price.body.ProductPriceDTO;
import com.shangpin.ephub.client.product.business.price.dto.PriceDto;
import com.shangpin.ephub.product.business.rest.price.dto.PriceQueryDto;
import com.shangpin.ephub.product.business.rest.price.vo.ProductPrice;
import com.shangpin.ephub.product.business.rest.price.vo.SpSeasonVo;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title: PriceService</p>
 * <p>Description: 供价推送服务类 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年3月31日 下午12:09:52
 *
 */
@Service
@Slf4j
public class PriceService {
	
	@Autowired
	private HubSupplierPriceChangeRecordGateWay priceChangeRecordGateWay;
	@Autowired
	private SupplierProductService supplierProductService;
	@Autowired
	private HubSeasonDicGateWay hubSeasonDicGateWay;
	
//	private PriceMqGateWay priceMqGateWay;
	
	public ProductPrice priceList(PriceQueryDto priceQueryDto){
		try {
			ProductPrice productPrice = new ProductPrice();
			HubSupplierPriceChangeRecordCriteriaDto criteriaDto = findCriteriaDto(priceQueryDto);
			if(null == criteriaDto){
				return null;
			}
			int total = priceChangeRecordGateWay.countByCriteria(criteriaDto);
			productPrice.setTotal(total); 
			if(total > 0){
				List<SpSeasonVo> productPriceList = new ArrayList<SpSeasonVo>();
				List<HubSupplierPriceChangeRecordDto> lists = priceChangeRecordGateWay.selectByCriteria(criteriaDto);
				for(HubSupplierPriceChangeRecordDto dto : lists){
					SpSeasonVo spSeasonVo = new SpSeasonVo();
					spSeasonVo.setSupplierId(dto.getSupplierId());
					spSeasonVo.setSupplierSeasonName(dto.getMarketSeason());
					spSeasonVo.setMarkPrice(null != dto.getMarketPrice() ? dto.getMarketPrice().toString() : "");
					spSeasonVo.setSpSkuId(dto.getSpSkuNo());
					spSeasonVo.setCurrency(dto.getCurrency());
					HubSupplierSpuDto supplierSpuDto = new HubSupplierSpuDto();
					supplierSpuDto.setSupplierId(dto.getSupplierId());
					supplierSpuDto.setSupplierSpuNo(dto.getSupplierSpuNo()); 
					List<HubSupplierSpuDto>  spus = supplierProductService.findHubSupplierSpuDtos(supplierSpuDto);
					if(CollectionUtils.isNotEmpty(spus)){
						HubSupplierSpuDto spu = spus.get(0);
						spSeasonVo.setBrandName(spu.getSupplierBrandname());
						spSeasonVo.setCategoryName(spu.getSupplierCategoryname()); 
					}
					HubSeasonDicDto seasonDic = findHubSeason(dto.getSupplierId(),dto.getMarketSeason());
					if(null != seasonDic){
						spSeasonVo.setSpSeasonName(seasonDic.getHubSeason());
						spSeasonVo.setSpSeasonYear(seasonDic.getHubMarketTime());
					}
					productPriceList.add(spSeasonVo);
				}
				productPrice.setProductPriceList(productPriceList);
			}
			return productPrice;
			
		} catch (Exception e) {
			log.error("查询共价变化记录出错："+e.getMessage(),e);
		}
		return null;
	}	
	public void savePriceRecordAndSendConsumer(PriceDto priceDto) throws Exception{
		HubSupplierSpuDto supplierSpuDto = priceDto.getHubSpu();
		List<HubSupplierSkuDto> supplierSkus = priceDto.getHubSkus();
		String supplierNo = priceDto.getSupplierNo();
		HubSupplierSpuDto spuDtoSel = supplierProductService.isSupplierSeasonNameChanged(supplierSpuDto);
		if(null != spuDtoSel){
			List<HubSupplierSkuDto> hubSkus = supplierProductService.findSupplierSkus(spuDtoSel.getSupplierSpuId());
			if(CollectionUtils.isNotEmpty(hubSkus)){
				for(HubSupplierSkuDto skuDto : hubSkus){
					if(!StringUtils.isEmpty(skuDto.getSpSkuNo())){
						HubSupplierPriceChangeRecordDto recordDto = new HubSupplierPriceChangeRecordDto();
						convertPriceDtoToRecordDto(supplierNo,supplierSpuDto,skuDto,recordDto,Type.SEASON);
						Long supplierPriceChangeRecordId = saveHubSupplierPriceChangeRecordDto(recordDto);
						ProductPriceDTO retryPrice  = new ProductPriceDTO();
						convertPriceDtoToRetryPrice(supplierNo,supplierSpuDto,skuDto,retryPrice);				
						sendMessageToPriceConsumer(supplierPriceChangeRecordId,retryPrice);
					}
				}
			}
		}
		for(HubSupplierSkuDto skuDto : supplierSkus){
			boolean isChanged = supplierProductService.isPriceChanged(skuDto);
			if(isChanged && !StringUtils.isEmpty(skuDto.getSpSkuNo())){
				HubSupplierPriceChangeRecordDto recordDto = new HubSupplierPriceChangeRecordDto();
				convertPriceDtoToRecordDto(supplierNo,supplierSpuDto,skuDto,recordDto,Type.PRICE);
				Long supplierPriceChangeRecordId = saveHubSupplierPriceChangeRecordDto(recordDto);
				ProductPriceDTO retryPrice  = new ProductPriceDTO();
				convertPriceDtoToRetryPrice(supplierNo,supplierSpuDto,skuDto,retryPrice);
				sendMessageToPriceConsumer(supplierPriceChangeRecordId,retryPrice);
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
	 * @param type 类型
	 */
	public void convertPriceDtoToRecordDto(String supplierNo,HubSupplierSpuDto supplierSpuDto,HubSupplierSkuDto supplierSkuDto,HubSupplierPriceChangeRecordDto recordDto,Type type){
		recordDto.setSupplierId(supplierSkuDto.getSupplierId());
		recordDto.setSupplierNo(supplierNo);
		recordDto.setSupplierSkuNo(supplierSkuDto.getSupplierSkuNo());
		recordDto.setSupplierSpuNo(supplierSpuDto.getSupplierSpuNo());
		recordDto.setSpSkuNo(supplierSkuDto.getSpSkuNo());
		recordDto.setMarketPrice(supplierSkuDto.getMarketPrice());
		recordDto.setSupplyPrice(supplierSkuDto.getSupplyPrice());
		recordDto.setCurrency(StringUtils.isEmpty(supplierSkuDto.getMarketPriceCurrencyorg()) ? supplierSkuDto.getSupplyPriceCurrency() : supplierSkuDto.getMarketPriceCurrencyorg()); 
		recordDto.setMarketSeason(supplierSpuDto.getSupplierSeasonname()); 
		recordDto.setState(State.UNHANDLED.getIndex());
		recordDto.setType(type.getIndex()); 
		recordDto.setCreateTime(new Date());
	}
	
	/**
	 * 保存供价记录
	 * @param recordDto
	 * @return
	 * @throws Exception
	 */
	public Long saveHubSupplierPriceChangeRecordDto(HubSupplierPriceChangeRecordDto recordDto) throws Exception{
		return priceChangeRecordGateWay.insert(recordDto);
	}
	/**
	 * 发送消息
	 * @param retryPrice
	 */
	public void sendMessageToPriceConsumer(Long supplierPriceChangeRecordId, ProductPriceDTO retryPrice) throws Exception{
		try {
			
			updateState(supplierPriceChangeRecordId,State.PUSHED);
		} catch (Exception e) {
			updateState(supplierPriceChangeRecordId,State.PUSHED_ERROR);
			throw new Exception("供价记录推送消息队列失败，supplierPriceChangeRecordId："+supplierPriceChangeRecordId+"，异常信息："+e.getMessage());
		}
	}
	/**
	 * 更新记录状态
	 * @param supplierPriceChangeRecordId
	 * @param state
	 */
	public void updateState(Long supplierPriceChangeRecordId,State state){
		HubSupplierPriceChangeRecordDto recordDto = new HubSupplierPriceChangeRecordDto();
		recordDto.setSupplierPriceChangeRecordId(supplierPriceChangeRecordId);
		recordDto.setState(state.getIndex()); 
		priceChangeRecordGateWay.updateByPrimaryKeySelective(recordDto);
	}
	
	private HubSupplierPriceChangeRecordCriteriaDto findCriteriaDto(PriceQueryDto priceQueryDto){
		HubSupplierPriceChangeRecordCriteriaDto criteriaDto = new HubSupplierPriceChangeRecordCriteriaDto();
		if(!StringUtils.isEmpty(priceQueryDto.getSpSkuId())){
			criteriaDto.createCriteria().andSpSkuNoEqualTo(priceQueryDto.getSpSkuId());
			return criteriaDto;
		}else{
			if(null != priceQueryDto.getPageIndex() && null != priceQueryDto.getPageSize()){
				criteriaDto.setPageNo(priceQueryDto.getPageIndex());
				criteriaDto.setPageSize(priceQueryDto.getPageSize());
				if(!StringUtils.isEmpty(priceQueryDto.getSupplierId())){
					criteriaDto.createCriteria().andSupplierIdEqualTo(priceQueryDto.getSupplierId());
				}
				if(!StringUtils.isEmpty(priceQueryDto.getMarketSeason())){
					criteriaDto.createCriteria().andMarketSeasonEqualTo(priceQueryDto.getMarketSeason());
				}
				if(!StringUtils.isEmpty(priceQueryDto.getMarketYear())){
					criteriaDto.createCriteria().andMarketYearEqualTo(priceQueryDto.getMarketYear());
				}
				return criteriaDto;
			}else{
				return null;
			}
		}
		
	}
	
	private HubSeasonDicDto findHubSeason(String supplierId,String supplierSeason){
		HubSeasonDicCriteriaDto criteriaDto = new HubSeasonDicCriteriaDto(); 
		criteriaDto.createCriteria().andSupplieridEqualTo(supplierId).andSupplierSeasonEqualTo(supplierSeason);
		List<HubSeasonDicDto> lists = hubSeasonDicGateWay.selectByCriteria(criteriaDto);
		if(CollectionUtils.isNotEmpty(lists)){
			return lists.get(0);
		}else{
			return null;
		}
	}
	
}
