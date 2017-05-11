package com.shangpin.ephub.product.business.rest.price.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.consumer.price.dto.ProductPriceDTO;
import com.shangpin.ephub.client.consumer.price.gateway.PriceMqGateWay;
import com.shangpin.ephub.client.data.mysql.enumeration.PriceHandleState;
import com.shangpin.ephub.client.data.mysql.enumeration.PriceHandleType;
import com.shangpin.ephub.client.data.mysql.price.unionselect.dto.PriceQueryDto;
import com.shangpin.ephub.client.data.mysql.price.unionselect.gateway.HubSupplierPriceGateWay;
import com.shangpin.ephub.client.data.mysql.price.unionselect.result.HubSupplierPrice;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierPriceChangeRecordCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierPriceChangeRecordDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierPriceChangeRecordGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.product.business.price.dto.PriceDto;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.conf.mail.message.ShangpinMail;
import com.shangpin.ephub.product.business.conf.mail.sender.ShangpinMailSender;
import com.shangpin.ephub.product.business.rest.price.dto.PriceQuery;
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
	private HubSupplierPriceGateWay hubSupplierPriceGateWay;
	@Autowired
	private ShangpinMailSender shangpinMailSender;
	
	/**
	 * 保存价格并推送消息
	 * @param priceDto
	 * @throws Exception
	 */
	public void savePriceRecordAndSendConsumer(PriceDto priceDto) throws Exception{
		HubSupplierSpuDto supplierSpuDto = priceDto.getHubSpu();
		String supplierNo = priceDto.getSupplierNo();
		
		//先判断季节是否发生变化
		Map<String,HubSupplierSkuDto> newSeasons = new HashMap<String,HubSupplierSkuDto>();
		HubSupplierSpuDto spuDtoSel = supplierProductService.isSupplierSeasonNameChanged(supplierSpuDto);
		if(null != spuDtoSel){
			List<HubSupplierSkuDto> hubSkus = supplierProductService.findSupplierSkus(spuDtoSel.getSupplierSpuId());
			if(CollectionUtils.isNotEmpty(hubSkus)){
				for(HubSupplierSkuDto skuDto : hubSkus){
					if(!StringUtils.isEmpty(skuDto.getSpSkuNo())){
						log.info("【"+supplierSpuDto.getSupplierId()+" "+skuDto.getSupplierSkuNo()+" 尚品sku："+skuDto.getSpSkuNo()+" 新季节："+supplierSpuDto.getSupplierSeasonname()+"<====>老季节："+spuDtoSel.getSupplierSeasonname()+"供应商spu编号："+supplierSpuDto.getSupplierSpuNo()+"】");  
						newSeasons.put(skuDto.getSupplierSkuNo(), skuDto);
					}
				}
			}
		}
		//再判断价格是否发生变化
		List<HubSupplierSkuDto> supplierSkus = priceDto.getHubSkus();
		for(HubSupplierSkuDto skuDto : supplierSkus){
			boolean isChanged = supplierProductService.isPriceChanged(skuDto);
			if(isChanged && !StringUtils.isEmpty(skuDto.getSpSkuNo())){
				log.info("【"+skuDto.getSupplierId()+" "+skuDto.getSupplierSkuNo()+" 尚品sku："+skuDto.getSpSkuNo()+" 新市场价："+skuDto.getMarketPrice()+" 新供价："+skuDto.getSupplyPrice()+" 价格发生了变化】"); 
				if(newSeasons.containsKey(skuDto.getSupplierSkuNo())){
					savePriceRecordAndSendConsumer(supplierSpuDto, supplierNo, skuDto,PriceHandleType.PRICEANDSEASON);
					newSeasons.remove(skuDto.getSupplierSkuNo());
				}else{
					savePriceRecordAndSendConsumer(supplierSpuDto, supplierNo, skuDto,PriceHandleType.PRICE);
				}
			}
		}
		if(newSeasons.size() > 0){
			for(HubSupplierSkuDto skuDto : newSeasons.values()){
				savePriceRecordAndSendConsumer(supplierSpuDto, supplierNo, skuDto,PriceHandleType.SEASON);
			}
		}
	}

	/**
	 * 将季节或价格发生变化的sku保存数据库并发送消息队列
	 * @param supplierSpuDto 供应商原始Spu信息
	 * @param supplierNo 供应商编号
	 * @param skuDto 供应商原始Sku信息
	 * @param type 记录的类型
	 * @throws Exception
	 */
	public void savePriceRecordAndSendConsumer(HubSupplierSpuDto supplierSpuDto, String supplierNo, HubSupplierSkuDto skuDto,PriceHandleType type)
			throws Exception {
		if(StringUtils.isEmpty(skuDto.getSpSkuNo())){
			return;
		}
		//查尚品的季节
		HubSeasonDicDto seasonDicDto = supplierProductService.findHubSeason(skuDto.getSupplierId(), supplierSpuDto.getSupplierSeasonname());
		//保存数据库
		HubSupplierPriceChangeRecordDto recordDto = new HubSupplierPriceChangeRecordDto();
		convertPriceDtoToRecordDto(supplierNo,supplierSpuDto,skuDto,recordDto, type,seasonDicDto);
		Long supplierPriceChangeRecordId = saveHubSupplierPriceChangeRecordDto(recordDto);
		log.info("【"+skuDto.getSupplierId()+" "+skuDto.getSupplierSkuNo()+"保存hub_supplier_price_change_record成功 "+supplierPriceChangeRecordId+"】");
		//发送消息队列
		ProductPriceDTO productPrice  = new ProductPriceDTO();
		convertPriceDtoToProductPriceDTO(supplierNo,skuDto,productPrice,seasonDicDto);
		sendMessageToPriceConsumer(supplierNo,supplierPriceChangeRecordId,productPrice);
		log.info("【"+skuDto.getSupplierId()+" "+skuDto.getSupplierSkuNo()+"发送消息队列成功 "+supplierPriceChangeRecordId+"】");
	}
	
	/**
	 * scm调用
	 * @param priceQuery
	 * @return
	 */
	public ProductPrice priceList(PriceQuery priceQuery){
		try {
			log.info("供价记录查询参数："+JsonUtil.serialize(priceQuery));  
			if(null == priceQuery.getPageIndex() && null == priceQuery.getPageSize() && CollectionUtils.isEmpty(priceQuery.getSpSkuIds())){
				return null;
			}
			PriceQueryDto priceQueryDto = copyPriceQueryToPriceQueryDto(priceQuery);
			log.info("转换后的查询参数："+JsonUtil.serialize(priceQueryDto));  
			ProductPrice productPrice = new ProductPrice();
			int total = hubSupplierPriceGateWay.countByQuery(priceQueryDto);
			log.info("供价记录查询总数："+total); 
			productPrice.setTotal(total); 
			if(total > 0){
				List<HubSupplierPrice> productPriceList = hubSupplierPriceGateWay.selectByQuery(priceQueryDto);
				productPrice.setProductPriceList(productPriceList); 
			}
			log.info("供价记录查询返回数据："+JsonUtil.serialize(productPrice));  
			return productPrice;
		} catch (Exception e) {
			log.error("查询共价变化记录出错："+e.getMessage(),e);
		}
		return null;
	}	
	
	private PriceQueryDto copyPriceQueryToPriceQueryDto(PriceQuery priceQuery){
		PriceQueryDto priceQueryDto = new PriceQueryDto();
		if(null != priceQuery){
			if(null != priceQuery.getPageIndex() && null != priceQuery.getPageSize()){
				priceQueryDto.setPageIndex(priceQuery.getPageIndex());
				priceQueryDto.setPageSize(priceQuery.getPageSize());
			}
			if(!StringUtils.isEmpty(priceQuery.getSupplierId())){
				priceQueryDto.setSupplierId(priceQuery.getSupplierId());
			}
			if(!StringUtils.isEmpty(priceQuery.getMarketSeason())){
				priceQueryDto.setMarketSeason(priceQuery.getMarketSeason());
			}
			if(!StringUtils.isEmpty(priceQuery.getMarketYear())){
				priceQueryDto.setMarketYear(priceQuery.getMarketYear());
			}
			if(CollectionUtils.isNotEmpty((priceQuery.getSpSkuIds()))){
				priceQueryDto.setSpSkuIds(priceQuery.getSpSkuIds()); 
			}
		}
		return priceQueryDto;
	}
	
	/**
	 * 将供应商原始表的信息转换为供价记录消息体
	 * @param supplierNo 供应商门户编号
	 * @param supplierSkuDto 供应商原始sku表对象
	 * @param productPrice 供价记录消息体
	 * @param seasonDicDto 尚品季节信息
	 */
	public void convertPriceDtoToProductPriceDTO(String supplierNo,HubSupplierSkuDto supplierSkuDto,ProductPriceDTO productPrice,HubSeasonDicDto seasonDicDto){
		productPrice.setSopUserNo(supplierSkuDto.getSupplierId());
		productPrice.setSkuNo(supplierSkuDto.getSpSkuNo());
		productPrice.setSupplierSkuNo(supplierSkuDto.getSupplierSkuNo());
		BigDecimal marketPrice = supplierSkuDto.getMarketPrice();
		productPrice.setMarketPrice(null != marketPrice ? marketPrice.toString() : ""); 
		BigDecimal supplyPrice = supplierSkuDto.getSupplyPrice();
		productPrice.setPurchasePrice(null != supplyPrice ? supplyPrice.toString() : "");
		productPrice.setMarketSeason(null != seasonDicDto ? seasonDicDto.getHubSeason() : "");
		productPrice.setMarketYear(null != seasonDicDto ? seasonDicDto.getHubMarketTime() : ""); 
		productPrice.setCurrency(StringUtils.isEmpty(supplierSkuDto.getMarketPriceCurrencyorg()) ? supplierSkuDto.getSupplyPriceCurrency() : supplierSkuDto.getMarketPriceCurrencyorg());
	}
	/**
	 * 将供应商原始表的信息转换为供价记录表实体类
	 * @param supplierNo 供应商门户编号
	 * @param supplierSpuDto 供应商原始spu表对象
	 * @param supplierSkuDto 供应商原始sku表对象
	 * @param recordDto 供价记录表实体类
	 * @param type 类型
	 * @param seasonDicDto 尚品季节信息
	 */
	public void convertPriceDtoToRecordDto(String supplierNo,HubSupplierSpuDto supplierSpuDto,HubSupplierSkuDto supplierSkuDto,HubSupplierPriceChangeRecordDto recordDto,PriceHandleType type,HubSeasonDicDto seasonDicDto){
		recordDto.setSupplierId(supplierSkuDto.getSupplierId());
		recordDto.setSupplierNo(supplierNo);
		recordDto.setSupplierSkuNo(supplierSkuDto.getSupplierSkuNo());
		recordDto.setSupplierSpuNo(supplierSpuDto.getSupplierSpuNo());
		recordDto.setSpSkuNo(supplierSkuDto.getSpSkuNo());
		recordDto.setMarketPrice(supplierSkuDto.getMarketPrice());
		recordDto.setSupplyPrice(supplierSkuDto.getSupplyPrice());
		recordDto.setCurrency(StringUtils.isEmpty(supplierSkuDto.getMarketPriceCurrencyorg()) ? supplierSkuDto.getSupplyPriceCurrency() : supplierSkuDto.getMarketPriceCurrencyorg()); 
		recordDto.setSupplierSeason(supplierSpuDto.getSupplierSeasonname()); 
		recordDto.setState(PriceHandleState.UNHANDLED.getIndex());
		recordDto.setType(type.getIndex()); 
		recordDto.setMarketSeason(null != seasonDicDto ? seasonDicDto.getHubSeason() : "");
		recordDto.setMarketYear(null != seasonDicDto ? seasonDicDto.getHubMarketTime() : ""); 
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

	public void sendMessageToPriceConsumer(String supplierNo, Long supplierPriceChangeRecordId, ProductPriceDTO retryPrice) throws Exception{
		try {
			retryPrice.setSupplierNo(supplierNo);
			retryPrice.setSupplierPriceChangeRecordId(supplierPriceChangeRecordId); 
			priceMqGateWay.transPrice(retryPrice);
			updateState(supplierPriceChangeRecordId,PriceHandleState.PUSHED);
		} catch (Exception e) {
			updateState(supplierPriceChangeRecordId,PriceHandleState.PUSHED_ERROR);
			log.error("【推送失败的消息是："+JsonUtil.serialize(retryPrice)+"】");
			String text = "供价记录推送消息队列失败，supplierPriceChangeRecordId："+supplierPriceChangeRecordId+"，异常信息："+e.getMessage()
			+"<br>"
			+"【推送失败的消息是："+JsonUtil.serialize(retryPrice)+"】"; 
			sendMail("供价记录推送消息队列失败",text);
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
		recordDto.setUpdateTime(new Date());
		priceChangeRecordGateWay.updateByPrimaryKeySelective(recordDto);
	}

	/**
	 *
	 * @param supplierPriceChangeRecordId
	 * @param state
	 * @param memo
	 */
	public void updateState(Long supplierPriceChangeRecordId,PriceHandleState state,String memo){
		HubSupplierPriceChangeRecordDto recordDto = new HubSupplierPriceChangeRecordDto();
		recordDto.setSupplierPriceChangeRecordId(supplierPriceChangeRecordId);
		recordDto.setState(state.getIndex());
		recordDto.setMemo(memo);
		recordDto.setUpdateTime(new Date());
		priceChangeRecordGateWay.updateByPrimaryKeySelective(recordDto);
	}

	/**
	 * 根据供货商门户编号 尚品编号  更新状态
	 * @param supplierId
	 * @param skuNo
	 * @param memo
	 * @param state
	 * @throws Exception
	 */
	public void updateState(String supplierId,String skuNo,String memo,PriceHandleState state) throws Exception{
		//查询最后一条记录
		HubSupplierPriceChangeRecordCriteriaDto criteria  = new HubSupplierPriceChangeRecordCriteriaDto();
		criteria.setOrderByClause(" create_time desc ");
		criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSpSkuNoEqualTo(skuNo).andStateEqualTo(PriceHandleState.PUSHED_OPENAPI_SUCCESS.getIndex());
		List<HubSupplierPriceChangeRecordDto> hubSupplierPriceChangeRecordDtos = priceChangeRecordGateWay.selectByCriteria(criteria);
		if(null!=hubSupplierPriceChangeRecordDtos&&hubSupplierPriceChangeRecordDtos.size()>0){
			HubSupplierPriceChangeRecordDto dto = hubSupplierPriceChangeRecordDtos.get(0);
			HubSupplierPriceChangeRecordDto tmp = new HubSupplierPriceChangeRecordDto();
			tmp.setState(state.getIndex());
			tmp.setMemo(memo);
			tmp.setUpdateTime(new Date());
			tmp.setSupplierPriceChangeRecordId(dto.getSupplierPriceChangeRecordId());
			priceChangeRecordGateWay.updateByPrimaryKeySelective(tmp);
		}


	}

	/**
	 * 发送邮件
	 * @param subject
	 * @param text
	 */
	public void sendMail(String subject,String text){
		try {
			ShangpinMail shangpinMail = new ShangpinMail();
			shangpinMail.setFrom("chengxu@shangpin.com");
			shangpinMail.setSubject(subject);
			shangpinMail.setText(text);
			shangpinMail.setTo("ephub_support.list@shangpin.com");
			shangpinMailSender.sendShangpinMail(shangpinMail);
		} catch (Exception e) {
			log.error("发送邮件失败："+e.getMessage(),e); 
		}
	}
	
}
