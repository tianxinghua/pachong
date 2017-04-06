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
import com.shangpin.ephub.client.product.business.price.dto.PriceDto;
import com.shangpin.ephub.client.product.business.price.gateway.PriceGateWay;
import com.shangpin.ephub.client.util.JsonUtil;
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
	 * @param hubSpu HubSupplierSpuDto对象
	 * @param hubSkus HubSupplierSkuDto对象集合
	 * @param pendingProduct 消息体对象
	 * @param headers 消息头
	 * @param supplierPicture 图片的消息体
	 */
	public boolean supplierSaveAndSendToPending(String supplierNo,String supplierId,String supplierName,HubSupplierSpuDto hubSpu,List<HubSupplierSkuDto> hubSkus,PendingProduct pendingProduct,Map<String,String> headers,SupplierPicture supplierPicture) throws EpHubSupplierProductConsumerException{
		try {
			//savePriceRecordAndSendConsumer(supplierNo,hubSpu,hubSkus);
			PendingSpu pendingSpu = new PendingSpu();		
			List<PendingSku> skus = new ArrayList<PendingSku>();
			//保存hubSpu到数据库
			ProductStatus productStatus = supplierProductMysqlService.isHubSpuChanged(supplierNo,hubSpu,pendingSpu);
			//开始构造消息头
			Spu spuHead = setSpuHead(supplierId,hubSpu.getSupplierSpuNo(),productStatus.getIndex());
			List<Sku> headSkus = new ArrayList<Sku>();		
			if(hubSkus != null && hubSkus.size()>0){
				for(HubSupplierSkuDto hubSku : hubSkus){
					try {					
						Sku headSku = new Sku();
						PendingSku pendingSku = new PendingSku();
						//开始保存hubSku到数据库
						hubSku.setSupplierSpuId(hubSpu.getSupplierSpuId()); //在这里回写supplierSpuId
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
			if(isCurrentSeason(hubSpu.getSupplierId(), hubSpu.getSupplierSeasonname())){
				if(null != supplierPicture){
					supplierPicture.setSupplierSpuId(hubSpu.getSupplierSpuId()); 
					pictureProductService.sendSupplierPicture(supplierPicture, null); 
				}
				return true;
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
	public boolean isCurrentSeason(String supplierId,String supplierSeason){
		if(StringUtils.isEmpty(supplierSeason)){
			return false;
		}else{
			supplierSeason = supplierSeason.trim();
			HubSeasonDicCriteriaDto criteriaDto = new HubSeasonDicCriteriaDto();
			criteriaDto.createCriteria().andSupplieridEqualTo(supplierId).andSupplierSeasonEqualTo(supplierSeason).andFilterFlagEqualTo((byte)1);
			List<HubSeasonDicDto> dics = seasonClient.selectByCriteria(criteriaDto);
			if(null != dics && dics.size() > 0){
				return true;
			}else{
				return false;
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
