package com.shangpin.supplier.product.consumer.service;

import com.shangpin.ephub.client.consumer.price.dto.ProductPriceDTO;
import com.shangpin.ephub.client.consumer.price.gateway.PriceMqGateWay;
import com.shangpin.ephub.client.data.mysql.enumeration.PriceHandleState;
import com.shangpin.ephub.client.data.mysql.enumeration.PriceHandleType;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierPriceChangeRecordCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierPriceChangeRecordDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierPriceChangeRecordGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;

import com.shangpin.ephub.client.product.business.mail.gateway.ShangpinMailSenderGateWay;
import com.shangpin.ephub.client.product.business.price.dto.PriceDto;
import com.shangpin.ephub.client.util.JsonUtil;

import com.shangpin.supplier.product.consumer.conf.mail.ShangpinMailProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private SupplierProductMysqlService supplierProductService;
	@Autowired
	private PriceMqGateWay priceMqGateWay;

	@Autowired
	ShangpinMailSenderGateWay shangpinMailSenderGateWay;


	@Autowired
	ShangpinMailProperties shangpinMailProperties;
	/**
	 * 保存价格并推送消息
	 * @param priceVO
	 * @throws Exception
	 */
	public void savePriceRecordAndSendConsumer(PriceDto priceVO) throws Exception{
		HubSupplierSpuDto supplierSpuVO = priceVO.getHubSpu();
		String supplierNo = priceVO.getSupplierNo();
		HubSupplierSpuDto supplierSpuDto = supplierProductService.getSupplierSpuBySupplierIdAndSupplierSpuNo(supplierSpuVO.getSupplierId(),supplierSpuVO.getSupplierSpuNo());
        if(null==supplierSpuDto) return ;//新品 不需要推送
		supplierSpuDto.setSupplierSpuNo(supplierSpuVO.getSupplierSpuNo());//查询时过滤了supplierspuno 补上
		List<HubSupplierSkuDto> supplierSkuDtoList = supplierProductService.findSupplierSkusSpSkuNoIsNotNull(supplierSpuDto.getSupplierSpuId());
		Map<String,HubSupplierSkuDto> skuDtoMap = new HashMap<>();
		if(CollectionUtils.isEmpty(supplierSkuDtoList)) return;//尚未有供货商SKU生成尚品的SKU
		supplierSkuDtoList.forEach(sku->{
			skuDtoMap.put(sku.getSupplierSkuNo(),sku);
		});


		//先判断季节是否发生变化

		Map<String,HubSupplierSkuDto> newSeasons = new HashMap<String,HubSupplierSkuDto>();
		if(this.isSupplierSeasonNameChanged(supplierSpuVO,supplierSpuDto)){
				for(HubSupplierSkuDto skuDto : supplierSkuDtoList){
					newSeasons.put(skuDto.getSupplierSkuNo(), skuDto);
				}
		}
		//再判断价格是否发生变化
		List<HubSupplierSkuDto> supplierSkuVOs = priceVO.getHubSkus();
		for(HubSupplierSkuDto skuVO : supplierSkuVOs){
			boolean supplyPriceChanged = supplierProductService.isSupplyPriceChanged(skuVO,skuDtoMap);
			boolean marketPriceChanged = supplierProductService.isMarketPriceChanged(skuVO,skuDtoMap);
			if(marketPriceChanged && supplyPriceChanged && newSeasons.containsKey(skuVO.getSupplierSkuNo())){
				log.info("【推送供价记录："+skuVO.getSupplierId()+" "+skuVO.getSupplierSkuNo()+" 尚品sku："+skuVO.getSpSkuNo()+"市场价、供价、季节都发生了变化。 新市场价："+skuVO.getMarketPrice()+" 新供价："+skuVO.getSupplyPrice()+" 新季节："+supplierSpuDto.getSupplierSeasonname()+"】");
				savePriceRecordAndSendConsumer(supplierSpuDto, supplierNo, skuVO,PriceHandleType.MARKET_SUPPLY_SEASON_CHANGED);
				newSeasons.remove(skuVO.getSupplierSkuNo());
			}else if(marketPriceChanged && supplyPriceChanged){
				log.info("【推送供价记录："+skuVO.getSupplierId()+" "+skuVO.getSupplierSkuNo()+" 尚品sku："+skuVO.getSpSkuNo()+"市场价、供价发生了变化。 新市场价："+skuVO.getMarketPrice()+"】");
				savePriceRecordAndSendConsumer(supplierSpuDto, supplierNo, skuVO,PriceHandleType.MARKET_SUPPLY_CHANGED);
			}else if(marketPriceChanged && newSeasons.containsKey(skuVO.getSupplierSkuNo())){
				log.info("【推送供价记录："+skuVO.getSupplierId()+" "+skuVO.getSupplierSkuNo()+" 尚品sku："+skuVO.getSpSkuNo()+"市场价、季节发生了变化。 新市场价："+skuVO.getMarketPrice()+" 新季节："+supplierSpuDto.getSupplierSeasonname()+"】");
				savePriceRecordAndSendConsumer(supplierSpuDto, supplierNo, skuVO,PriceHandleType.MARKET_SEASON_CHANGED);
				newSeasons.remove(skuVO.getSupplierSkuNo());
			}else if(supplyPriceChanged && newSeasons.containsKey(skuVO.getSupplierSkuNo())){
				log.info("【推送供价记录："+skuVO.getSupplierId()+" "+skuVO.getSupplierSkuNo()+" 尚品sku："+skuVO.getSpSkuNo()+"供价、季节发生了变化。 新供价："+skuVO.getSupplyPrice()+" 新季节："+supplierSpuDto.getSupplierSeasonname()+"】");
				savePriceRecordAndSendConsumer(supplierSpuDto, supplierNo, skuVO,PriceHandleType.SUPPLY_SEASON_CHANGED);
				newSeasons.remove(skuVO.getSupplierSkuNo());
			}else if(marketPriceChanged){
				log.info("【推送供价记录："+skuVO.getSupplierId()+" "+skuVO.getSupplierSkuNo()+" 尚品sku："+skuVO.getSpSkuNo()+"市场价发生了变化。 新市场价："+skuVO.getMarketPrice()+"】");
				savePriceRecordAndSendConsumer(supplierSpuDto, supplierNo, skuVO,PriceHandleType.MARKET_PRICE_CHANGED);
			}else if(supplyPriceChanged){
				log.info("【推送供价记录："+skuVO.getSupplierId()+" "+skuVO.getSupplierSkuNo()+" 尚品sku："+skuVO.getSpSkuNo()+"供价发生了变化。 新供价："+skuVO.getSupplyPrice()+" 】");
				savePriceRecordAndSendConsumer(supplierSpuDto, supplierNo, skuVO,PriceHandleType.SUPPLY_PRICE_CHANGED);
			}
		}
		if(newSeasons.size() > 0){
			for(HubSupplierSkuDto skuDto : newSeasons.values()){
				log.info("【推送供价记录："+supplierSpuDto.getSupplierId()+" "+skuDto.getSupplierSkuNo()+" 尚品sku："+skuDto.getSpSkuNo()+"只有季节发生了变化。 新季节："+supplierSpuVO.getSupplierSeasonname()+"<====>老季节："+supplierSpuDto.getSupplierSeasonname()+"供应商spu编号："+supplierSpuDto.getSupplierSpuNo()+"】");
				savePriceRecordAndSendConsumer(supplierSpuDto, supplierNo, skuDto,PriceHandleType.SEASON_CHANGED);
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
		log.info("【推送供价记录："+skuDto.getSupplierId()+" "+skuDto.getSupplierSkuNo()+"保存hub_supplier_price_change_record成功 "+supplierPriceChangeRecordId+"】");
		//发送消息队列
		ProductPriceDTO productPrice  = new ProductPriceDTO();
		convertPriceDtoToProductPriceDTO(supplierNo,skuDto,productPrice,type, seasonDicDto);
		sendMessageToPriceConsumer(supplierPriceChangeRecordId,productPrice);
		log.info("【推送供价记录："+skuDto.getSupplierId()+" "+skuDto.getSupplierSkuNo()+"发送消息队列成功 "+supplierPriceChangeRecordId+"】");
	}

	
	/**
	 * 将供应商原始表的信息转换为供价记录消息体
	 * @param supplierNo 供应商门户编号
	 * @param supplierSkuDto 供应商原始sku表对象
	 * @param productPrice 供价记录消息体
	 * @param type 记录类型
	 * @param seasonDicDto 尚品季节信息
	 */
	public void convertPriceDtoToProductPriceDTO(String supplierNo,HubSupplierSkuDto supplierSkuDto,ProductPriceDTO productPrice,PriceHandleType type,HubSeasonDicDto seasonDicDto){
		productPrice.setPriceHandleType(type.getIndex()); 
		productPrice.setSupplierNo(supplierNo); 
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
	 * 季节是否发生变化
	 * @param supplierSpuDto
	 * @return
	 * @throws Exception
	 */
	private  boolean  isSupplierSeasonNameChanged(HubSupplierSpuDto supplierSpuVO, HubSupplierSpuDto supplierSpuDto) throws Exception {
        String seasonNameVo = supplierSpuVO.getSupplierSeasonname();
        String seasonNameDto = supplierSpuDto.getSupplierSeasonname();
        if(null==seasonNameVo)  seasonNameVo = "";
		if(null==seasonNameDto)  seasonNameDto = "";

		if(seasonNameVo.equals(seasonNameDto)){
			return false;
		}else{
			return true;
		}
	}



	/**
	 * 发送消息
	 * @param productPriceDto
	 */

	public void sendMessageToPriceConsumer(Long supplierPriceChangeRecordId, ProductPriceDTO productPriceDto) throws Exception{
		try {
			BigDecimal threshold = new BigDecimal("10.00");
			String marketPriceStr = productPriceDto.getMarketPrice();
			if(!StringUtils.isEmpty(marketPriceStr)){
				BigDecimal marketPrice = new BigDecimal(marketPriceStr);
				if(marketPrice.compareTo(threshold) == 1){
					productPriceDto.setSupplierPriceChangeRecordId(supplierPriceChangeRecordId);
					log.info("【推送供价消息体："+JsonUtil.serialize(productPriceDto)+"】");
					priceMqGateWay.transPrice(productPriceDto);
					updateState(supplierPriceChangeRecordId,PriceHandleState.PUSHED);
				}
			}

		} catch (Exception e) {
			updateState(supplierPriceChangeRecordId,PriceHandleState.PUSHED_ERROR);
			log.error("【推送失败的消息是："+JsonUtil.serialize(productPriceDto)+"】");
			String text = "供价记录推送消息队列失败，supplierPriceChangeRecordId："+supplierPriceChangeRecordId+"，异常信息："+e.getMessage()
					+"<br>"
					+"【推送失败的消息是："+JsonUtil.serialize(productPriceDto)+"】";
			sendMail("供价记录推送消息队列失败",text);
			throw new Exception("供价记录推送消息队列失败，supplierPriceChangeRecordId："+supplierPriceChangeRecordId+"，异常信息："+e.getMessage());
		}
	}

	/**
	 * 发送邮件
	 * @param subject
	 * @param text
	 */



	public void sendMail(String subject,String text){
		try {
			com.shangpin.ephub.client.product.business.mail.dto.ShangpinMail shangpinMail = new com.shangpin.ephub.client.product.business.mail.dto.ShangpinMail();
			shangpinMail.setFrom("chengxu@shangpin.com");
			shangpinMail.setSubject(subject);
			shangpinMail.setText(text);
			shangpinMail.setTo(shangpinMailProperties.getMailSendTo());
			shangpinMailSenderGateWay.send(shangpinMail);
		} catch (Exception e) {
			log.error("发送邮件失败："+e.getMessage(),e);
		}
	}

	
}
