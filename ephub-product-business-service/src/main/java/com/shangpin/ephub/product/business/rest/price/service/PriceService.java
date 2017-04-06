package com.shangpin.ephub.product.business.rest.price.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.consumer.price.dto.ProductPriceDTO;
import com.shangpin.ephub.client.consumer.price.gateway.PriceMqGateWay;
import com.shangpin.ephub.client.data.mysql.enumeration.PriceHandleState;
import com.shangpin.ephub.client.data.mysql.enumeration.PriceHandleType;
import com.shangpin.ephub.client.data.mysql.price.unionselect.dto.PriceQueryDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierPriceChangeRecordDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierPriceChangeRecordGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.product.business.price.dto.PriceDto;
import com.shangpin.ephub.product.business.rest.price.vo.ProductPrice;

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
	private PriceMqGateWay priceMqGateWay;
	@Autowired 
	private HubSupplierSkuGateWay hubSupplierSkuGateWay;
	
	/**
	 * 保存价格并推送消息
	 * @param priceDto
	 * @throws Exception
	 */
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
						convertPriceDtoToRecordDto(supplierNo,supplierSpuDto,skuDto,recordDto, PriceHandleType.SEASON);
						Long supplierPriceChangeRecordId = saveHubSupplierPriceChangeRecordDto(recordDto);
						ProductPriceDTO productPrice  = new ProductPriceDTO();
						convertPriceDtoToRetryPrice(supplierNo,supplierSpuDto,skuDto,productPrice);				
						sendMessageToPriceConsumer(supplierPriceChangeRecordId,productPrice);
					}
				}
			}
		}
		for(HubSupplierSkuDto skuDto : supplierSkus){
			boolean isChanged = supplierProductService.isPriceChanged(skuDto);
			if(isChanged && !StringUtils.isEmpty(skuDto.getSpSkuNo())){
				HubSupplierPriceChangeRecordDto recordDto = new HubSupplierPriceChangeRecordDto();
				convertPriceDtoToRecordDto(supplierNo,supplierSpuDto,skuDto,recordDto,PriceHandleType.PRICE);
				Long supplierPriceChangeRecordId = saveHubSupplierPriceChangeRecordDto(recordDto);
				ProductPriceDTO productPrice  = new ProductPriceDTO();
				convertPriceDtoToRetryPrice(supplierNo,supplierSpuDto,skuDto,productPrice);
				sendMessageToPriceConsumer(supplierPriceChangeRecordId,productPrice);
			}
		}
	}
	
	/**
	 * scm调用
	 * @param priceQueryDto
	 * @return
	 */
	public ProductPrice priceList(PriceQueryDto priceQueryDto){
		try {
			ProductPrice productPrice = new ProductPrice();
//			HubSupplierSkuCriteriaDto criteriaDto = supplierProductService.findCriteriaDto(priceQueryDto);
//			if(null == criteriaDto){
//				return null;
//			}
//			int total = hubSupplierSkuGateWay.countByCriteria(criteriaDto);
//			productPrice.setTotal(total); 
//			List<SpSeasonVo> productPriceList = new ArrayList<SpSeasonVo>();
//			if(total > 0){
//				List<HubSupplierSkuDto> lists = hubSupplierSkuGateWay.selectByCriteria(criteriaDto);
//				for(HubSupplierSkuDto dto : lists){
//					SpSeasonVo spSeasonVo = new SpSeasonVo();
//					spSeasonVo.setSupplierId(dto.getSupplierId());
//					spSeasonVo.setMarkPrice(null != dto.getMarketPrice() ? dto.getMarketPrice().toString() : "");
//					spSeasonVo.setSpSkuId(null != dto.getSpSkuNo() ? dto.getSpSkuNo() : "");
//					HubSupplierSpuDto  spu = supplierProductService.findHubSupplierSpuDtos(dto.getSupplierSpuId());
//					spSeasonVo.setBrandName(null != spu.getSupplierBrandname() ? spu.getSupplierBrandname() : "");
//					spSeasonVo.setCategoryName(null != spu.getSupplierCategoryname() ? spu.getSupplierCategoryname() : ""); 
//					spSeasonVo.setSupplierSeasonName(null != spu.getSupplierSeasonname() ? spu.getSupplierSeasonname() : "");
//					if(!StringUtils.isEmpty(priceQueryDto.getMarketSeason()) && !StringUtils.isEmpty(priceQueryDto.getMarketYear())){
//						spSeasonVo.setSpSeasonName(priceQueryDto.getMarketSeason());
//						spSeasonVo.setSpSeasonYear(priceQueryDto.getMarketYear());
//					}else{
//						HubSeasonDicDto seasonDic = supplierProductService.findHubSeason(dto.getSupplierId(),spu.getSupplierSeasonname());
//						spSeasonVo.setSpSeasonName(null != seasonDic ? seasonDic.getHubSeason() : "");
//						spSeasonVo.setSpSeasonYear(null != seasonDic ? seasonDic.getHubMarketTime() : ""); 
//					}
//					spSeasonVo.setCurrency("");
//					productPriceList.add(spSeasonVo);
//				}
//				productPrice.setProductPriceList(productPriceList);
//			}
			return productPrice;
		} catch (Exception e) {
			log.error("查询共价变化记录出错："+e.getMessage(),e);
		}
		return null;
	}	
	
	/**
	 * 将供应商原始表的信息转换为供价记录消息体
	 * @param supplierNo 供应商门户编号
	 * @param supplierSpuDto 供应商原始spu表对象
	 * @param supplierSkuDto 供应商原始sku表对象
	 * @param retryPrice 供价记录消息体
	 */
	public void convertPriceDtoToRetryPrice(String supplierNo,HubSupplierSpuDto supplierSpuDto,HubSupplierSkuDto supplierSkuDto,ProductPriceDTO productPrice){
		productPrice.setSopUserNo(supplierSkuDto.getSupplierId());
		productPrice.setSkuNo(supplierSkuDto.getSpSkuNo());
		productPrice.setSupplierSkuNo(supplierSkuDto.getSupplierSkuNo());
		BigDecimal marketPrice = supplierSkuDto.getMarketPrice();
		productPrice.setMarketPrice(null != marketPrice ? marketPrice.toString() : ""); 
		BigDecimal supplyPrice = supplierSkuDto.getSupplyPrice();
		productPrice.setPurchasePrice(null != supplyPrice ? supplyPrice.toString() : "");
		productPrice.setMarketSeason(supplierSpuDto.getSupplierSeasonname());
		productPrice.setCurrency(StringUtils.isEmpty(supplierSkuDto.getMarketPriceCurrencyorg()) ? supplierSkuDto.getSupplyPriceCurrency() : supplierSkuDto.getMarketPriceCurrencyorg());
	}
	/**
	 * 将供应商原始表的信息转换为供价记录表实体类
	 * @param supplierNo 供应商门户编号
	 * @param supplierSpuDto 供应商原始spu表对象
	 * @param supplierSkuDto 供应商原始sku表对象
	 * @param recordDto 供价记录表实体类
	 * @param type 类型
	 */
	public void convertPriceDtoToRecordDto(String supplierNo,HubSupplierSpuDto supplierSpuDto,HubSupplierSkuDto supplierSkuDto,HubSupplierPriceChangeRecordDto recordDto,PriceHandleType type){
		recordDto.setSupplierId(supplierSkuDto.getSupplierId());
		recordDto.setSupplierNo(supplierNo);
		recordDto.setSupplierSkuNo(supplierSkuDto.getSupplierSkuNo());
		recordDto.setSupplierSpuNo(supplierSpuDto.getSupplierSpuNo());
		recordDto.setSpSkuNo(supplierSkuDto.getSpSkuNo());
		recordDto.setMarketPrice(supplierSkuDto.getMarketPrice());
		recordDto.setSupplyPrice(supplierSkuDto.getSupplyPrice());
		recordDto.setCurrency(StringUtils.isEmpty(supplierSkuDto.getMarketPriceCurrencyorg()) ? supplierSkuDto.getSupplyPriceCurrency() : supplierSkuDto.getMarketPriceCurrencyorg()); 
		recordDto.setMarketSeason(supplierSpuDto.getSupplierSeasonname()); 
		recordDto.setState(PriceHandleState.UNHANDLED.getIndex());
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
			priceMqGateWay.transPrice(retryPrice);
			updateState(supplierPriceChangeRecordId,PriceHandleState.PUSHED);
		} catch (Exception e) {
			updateState(supplierPriceChangeRecordId,PriceHandleState.PUSHED_ERROR);
			throw new Exception("供价记录推送消息队列失败，supplierPriceChangeRecordId："+supplierPriceChangeRecordId+"，异常信息："+e.getMessage());
		}
	}
	/**
	 * 更新记录状态
	 * @param supplierPriceChangeRecordId
	 * @param state
	 */
	public void updateState(Long supplierPriceChangeRecordId,PriceHandleState state){
		HubSupplierPriceChangeRecordDto recordDto = new HubSupplierPriceChangeRecordDto();
		recordDto.setSupplierPriceChangeRecordId(supplierPriceChangeRecordId);
		recordDto.setState(state.getIndex()); 
		priceChangeRecordGateWay.updateByPrimaryKeySelective(recordDto);
	}
	
//	private HubSupplierPriceChangeRecordCriteriaDto findCriteriaDto(PriceQueryDto priceQueryDto){
//		HubSupplierPriceChangeRecordCriteriaDto criteriaDto = new HubSupplierPriceChangeRecordCriteriaDto();
//		if(CollectionUtils.isNotEmpty(priceQueryDto.getSpSkuIds())){
//			criteriaDto.createCriteria().andSpSkuNoIn(priceQueryDto.getSpSkuIds());
//			return criteriaDto;
//		}else{
//			if(null != priceQueryDto.getPageIndex() && null != priceQueryDto.getPageSize()){
//				criteriaDto.setPageNo(priceQueryDto.getPageIndex());
//				criteriaDto.setPageSize(priceQueryDto.getPageSize());
//				if(!StringUtils.isEmpty(priceQueryDto.getSupplierId())){
//					criteriaDto.createCriteria().andSupplierIdEqualTo(priceQueryDto.getSupplierId());
//				}
//				if(!StringUtils.isEmpty(priceQueryDto.getMarketSeason())){
//					criteriaDto.createCriteria().andMarketSeasonEqualTo(priceQueryDto.getMarketSeason());
//				}
//				if(!StringUtils.isEmpty(priceQueryDto.getMarketYear())){
//					criteriaDto.createCriteria().andMarketYearEqualTo(priceQueryDto.getMarketYear());
//				}
//				return criteriaDto;
//			}else{
//				return null;
//			}
//		}
//		
//	}
	
	
}
