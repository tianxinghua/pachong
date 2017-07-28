package com.shangpin.supplier.product.consumer.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.enumeration.FilterFlag;
import com.shangpin.ephub.client.data.mysql.enumeration.PicState;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.season.gateway.HubSeasonDicGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.pending.body.PendingProduct;
import com.shangpin.ephub.client.message.pending.body.sku.PendingSku;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;
import com.shangpin.ephub.client.message.pending.header.MessageHeaderKey;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.product.business.mail.dto.ShangpinMail;
import com.shangpin.ephub.client.product.business.mail.gateway.ShangpinMailSenderGateWay;
import com.shangpin.ephub.client.product.business.price.dto.PriceDto;
import com.shangpin.ephub.client.product.business.price.gateway.PriceGateWay;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.conf.mail.ShangpinMailProperties;
import com.shangpin.supplier.product.consumer.enumeration.ProductStatus;
import com.shangpin.supplier.product.consumer.exception.EpHubSupplierProductConsumerException;
import com.shangpin.supplier.product.consumer.manager.SupplierProductRetryManager;
import com.shangpin.supplier.product.consumer.service.dto.Sku;
import com.shangpin.supplier.product.consumer.service.dto.Spu;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title:SupplierProductSaveAndSendToPending </p>
 * <p>Description: 各个供应商保存数据并且发消息给Pending</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月15日 下午2:23:47
 *
 */
@Service
@Slf4j
public class SupplierProductSaveAndSendToPending {
	
	@Autowired
	ShangpinMailSenderGateWay shangpinMailSenderGateWay;
	@Autowired
	private SupplierProductMysqlService supplierProductMysqlService;
	@Autowired
	private PictureProductService pictureProductService;
	@Autowired
	SupplierProductSendToPending supplierProductSendToPending;
	@Autowired
	private HubSeasonDicGateWay seasonClient;
	@Autowired
	private SupplierProductRetryManager supplierProductRetryManager;
	@Autowired
	private PriceGateWay priceGateWay;
	@Autowired
	ShangpinMailProperties shangpinMailProperties;
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
			shangpinMail.setTo(shangpinMailProperties.getMailSendTo());
			shangpinMailSenderGateWay.send(shangpinMail);
		} catch (Exception e) {
			log.error("发送邮件失败："+e.getMessage(),e); 
		}
	}
	public void saveAndSendToPending(String supplierNo,String supplierId,String supplierName,HubSupplierSpuDto hubSpu,List<HubSupplierSkuDto> hubSkus,SupplierPicture supplierPicture) throws EpHubSupplierProductConsumerException{
		
		//映射表里维护supplierId、supplierNo、supplierName
		HubSupplierValueMappingDto list = supplierProductRetryManager.findHubSupplierValueMapping(supplierId);
		if(list==null){
			HubSupplierValueMappingDto dto = new HubSupplierValueMappingDto();
			dto.setSupplierId(supplierId);
			dto.setHubVal(supplierName);
			dto.setHubValNo(supplierNo);
			dto.setHubValType((byte)5);
			dto.setCreateTime(new Date());
			dto.setUpdateTime(new Date());
			dto.setCreateUser("SupplierCousumerService");
			dto.setDataState((byte)1);
			supplierProductRetryManager.insert(dto);
		}
		if(org.apache.commons.lang.StringUtils.isNotBlank(hubSpu.getSupplierSeasonname())){
			HubSeasonDicDto hubSeason = supplierProductRetryManager.findSupplierSeason(supplierId, hubSpu.getSupplierSeasonname());
			if(hubSeason==null){
				hubSeason = new HubSeasonDicDto(); 
				hubSeason.setCreateTime(new Date());
				hubSeason.setCreateUser("SupplierConsumerService");
				hubSeason.setDataState((byte)1);
				hubSeason.setPushState((byte)0);
				hubSeason.setFilterFlag((byte)0);
				hubSeason.setSupplierid(supplierId);
				hubSeason.setSupplierSeason(hubSpu.getSupplierSeasonname());
				try{
					supplierProductRetryManager.insertHubSeasonDic(hubSeason);
					sendMail("供应商"+supplierName+"新增季节","供应商"+supplierNo+":"+supplierName+"新增季节："+hubSpu.getSupplierSeasonname()+",请在季节字典维护对应尚品季节");
				}catch(Exception e){
					log.info("季节新增失败：{}",e);
				}
			}
		}
		
		PendingProduct pendingProduct = initPendingProduct(supplierNo,supplierId, supplierName);
		Map<String,String> headers = new HashMap<String,String>();	
		boolean flag = supplierSaveAndSendToPending(supplierNo,supplierId, supplierName, hubSpu, hubSkus, pendingProduct, headers,supplierPicture); 
		if(flag){
			boolean result = supplierProductSendToPending.dispatchSupplierProduct(pendingProduct,headers);
			log.info("供应商："+supplierName+"编号："+supplierNo+" 保存数据成功，并发送消息队列返回结果："+result); 
		}else{
//			log.info("供应商："+supplierName+"编号："+supplierNo+" 保存数据成功，季节编号"+hubSpu.getSupplierSeasonname()+"非当季，未推送pending队列"); 
		}
	}
	
	/**
	 * 保存hubSpu以及hubSku，并且构造消息体和消息头
	 * @param supplierNo 供应商编号
	 * @param supplierId 供应商门户id
	 * @param supplierName 供应商名称
	 * @param supplierSpuDto HubSupplierSpuDto对象
	 * @param supplierSkuDtos HubSupplierSkuDto对象集合
	 * @param pendingProduct 消息体对象
	 * @param headers 消息头
	 * @param supplierPicture 图片的消息体
	 */
	public boolean supplierSaveAndSendToPending(String supplierNo,String supplierId,String supplierName,HubSupplierSpuDto supplierSpuDto,List<HubSupplierSkuDto> supplierSkuDtos,PendingProduct pendingProduct,Map<String,String> headers,SupplierPicture supplierPicture) throws EpHubSupplierProductConsumerException{
		try {
			savePriceRecordAndSendConsumer(supplierNo,supplierSpuDto,supplierSkuDtos);
			PendingSpu pendingSpu = new PendingSpu();		
			List<PendingSku> skus = new ArrayList<PendingSku>();
			//保存hubSpu到数据库
			if(null == supplierPicture || null == supplierPicture.getProductPicture() || CollectionUtils.isEmpty(supplierPicture.getProductPicture().getImages())){
				pendingSpu.setPicState(PicState.NO_PIC.getIndex());
			}
			ProductStatus productStatus = supplierProductMysqlService.isHubSpuChanged(supplierNo,supplierSpuDto,pendingSpu);
			//开始构造消息头
			Spu spuHead = setSpuHead(supplierId,supplierSpuDto.getSupplierSpuNo(),productStatus.getIndex());
			List<Sku> headSkus = new ArrayList<Sku>();		
			if(supplierSkuDtos != null && supplierSkuDtos.size()>0){
				for(HubSupplierSkuDto hubSku : supplierSkuDtos){
					try {					
						Sku headSku = new Sku();
						PendingSku pendingSku = new PendingSku();
						//开始保存hubSku到数据库
						hubSku.setSupplierSpuId(supplierSpuDto.getSupplierSpuId()); //在这里回写supplierSpuId
						ProductStatus skuStatus = supplierProductMysqlService.isHubSkuChanged(hubSku, pendingSku);
						skus.add(pendingSku);
						headSku.setSupplierId(supplierId);
						headSku.setSkuNo(hubSku.getSupplierSkuNo());
						headSku.setStatus(skuStatus.getIndex());
						headSkus.add(headSku);
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}				
				}
			}		
			pendingSpu.setSkus(skus);
			pendingProduct.setData(pendingSpu);		
			spuHead.setSkus(headSkus);	
			headers.put(MessageHeaderKey.PENDING_PRODUCT_MESSAGE_HEADER_KEY, JsonUtil.serialize(spuHead));
			//发送图片
			HubSeasonDicDto dicDto = isCurrentSeason(supplierSpuDto.getSupplierId(), supplierSpuDto.getSupplierSeasonname());
			if(null != dicDto && dicDto.getFilterFlag() == FilterFlag.EFFECTIVE.getIndex() && "1".equals(dicDto.getMemo())){
				if(null != supplierPicture){
					supplierPicture.setSupplierSpuId(supplierSpuDto.getSupplierSpuId()); 
					pictureProductService.sendSupplierPicture(supplierPicture, null); 
				}
				return true;
			}else if(null != dicDto && dicDto.getFilterFlag() == FilterFlag.EFFECTIVE.getIndex() && "0".equals(dicDto.getMemo())){
				if(null != supplierPicture){
					supplierPicture.setSupplierSpuId(supplierSpuDto.getSupplierSpuId()); 
					pictureProductService.sendSupplierPicture(supplierPicture, null); 
				}
			}
		} catch (Exception e) {
			throw new EpHubSupplierProductConsumerException(e.getMessage(),e);
		}
		return false;
	}
	
	/**
	 * 保存价格变化记录并且推送到价格消费者
	 * @param supplierNo 供应商编号
	 * @param hubSkus 供应商原始sku
	 */
	public void savePriceRecordAndSendConsumer(String supplierNo, HubSupplierSpuDto hubSpu, List<HubSupplierSkuDto> hubSkus){
		try {
			log.info("【"+hubSpu.getSupplierId()+" "+hubSpu.getSupplierSpuNo()+"开始推送供价变化记录：新季节是："+hubSpu.getSupplierSeasonname()+"】"); 
			if(CollectionUtils.isNotEmpty(hubSkus)){
				PriceDto priceDto = new PriceDto();
				priceDto.setSupplierNo(supplierNo);
				priceDto.setHubSpu(hubSpu);
				priceDto.setHubSkus(hubSkus);
				priceGateWay.savePriceRecordAndSendConsumer(priceDto);
			}
		} catch (Exception e) {
			log.error("【"+hubSpu.getSupplierId()+" "+hubSpu.getSupplierSpuNo()+"推送供价变化记录异常："+e.getMessage()+"】",e);  
		}
	}
	
	/**
	 * 是否当前季
	 * @param supplierId
	 * @param supplierSeason
	 * @return
	 */
	public HubSeasonDicDto isCurrentSeason(String supplierId,String supplierSeason){
		if(StringUtils.isEmpty(supplierSeason)){
			return null;
		}else{
			supplierSeason = supplierSeason.trim();
			HubSeasonDicCriteriaDto criteriaDto = new HubSeasonDicCriteriaDto();
			criteriaDto.createCriteria().andSupplieridEqualTo(supplierId).andSupplierSeasonEqualTo(supplierSeason).andFilterFlagIsNotNull().andMemoIsNotNull();
			List<HubSeasonDicDto> dics = seasonClient.selectByCriteria(criteriaDto);
			if(CollectionUtils.isNotEmpty(dics)){
				return dics.get(0); 
			}else{
				return null;
			}
		}
	}
	/**
	 * 赋值Spu并返回
	 * @param supplierId
	 * @param supplierSpuNo
	 * @param productStatus
	 * @return
	 */
	private Spu setSpuHead(String supplierId,String supplierSpuNo,int productStatus) throws EpHubSupplierProductConsumerException {
		Spu spuHead = new Spu();
		spuHead.setSupplierId(supplierId);
		spuHead.setSpuNo(supplierSpuNo);
		spuHead.setStatus(productStatus);
		return spuHead;
	}

	/**
	 * 初始化PendingProduct对象
	 * @param supplierId
	 * @param supplierName
	 * @return
	 */
	private PendingProduct initPendingProduct(String supplierNo,String supplierId, String supplierName) throws EpHubSupplierProductConsumerException {
		PendingProduct pendingProduct = new PendingProduct();
		pendingProduct.setSupplierNo(supplierNo); 
		pendingProduct.setSupplierId(supplierId);
		pendingProduct.setSupplierName(supplierName);
		return pendingProduct;
	}
}
