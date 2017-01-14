package com.shangpin.supplier.product.consumer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.pending.body.PendingProduct;
import com.shangpin.ephub.client.message.pending.body.sku.PendingSku;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;
import com.shangpin.ephub.client.message.pending.header.MessageHeaderKey;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.conf.stream.source.sender.PendingProductStreamSender;
import com.shangpin.supplier.product.consumer.enumeration.ProductStatus;
import com.shangpin.supplier.product.consumer.exception.EpHubSupplierProductConsumerException;
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
	private PendingProductStreamSender pendingProductStreamSender;
	@Autowired
	private PictureProductService pictureProductService;
	@Autowired
	SupplierProductSendToPending supplierProductSendToPending;
		
	@SuppressWarnings("unused")
	private void sendPending(String supplierName,String supplierNo,PendingProduct pendingProduct,Map<String,String> headers){
		boolean result = supplierProductSendToPending.dispatchSupplierProduct(pendingProduct,headers);
		log.info("供应商："+supplierName+"编号："+supplierNo+" 保存数据成功，并发送消息队列返回结果："+result); 
		
	}
	
	/**
	 * atelier系列供应商保存数据以及发送消息给Pending
	 * @param supplierNo 供应商编号
	 * @param supplierId 供应商门户编号
	 * @param supplierName
	 * @param hubSpu
	 * @param hubSkus
	 * @param supplierPicture 图片消息体
	 */
	public void atelierSaveAndSendToPending(String supplierNo,String supplierId,String supplierName,HubSupplierSpuDto hubSpu,List<HubSupplierSkuDto> hubSkus,SupplierPicture supplierPicture) throws EpHubSupplierProductConsumerException{
	
		PendingProduct pendingProduct = initPendingProduct(supplierNo,supplierId, supplierName);
		Map<String,String> headers = new HashMap<String,String>();	
		supplierSaveAndSendToPending(supplierId, supplierName, hubSpu, hubSkus, pendingProduct, headers,supplierPicture); 
		sendPending(supplierName,supplierNo,pendingProduct,headers);
	}
	/**
	 * spinnaker系列供应商保存数据以及发送消息给Pending
	 * @param supplierNo 供应商编号
	 * @param supplierId 供应商门户编号
	 * @param supplierName
	 * @param hubSpu
	 * @param hubSkus
	 * @param supplierPicture 图片消息体
	 */
	public void spinnakerSaveAndSendToPending(String supplierNo,String supplierId,String supplierName,HubSupplierSpuDto hubSpu,List<HubSupplierSkuDto> hubSkus,SupplierPicture supplierPicture) throws EpHubSupplierProductConsumerException{
		/**
		 * 消息体
		 */
		PendingProduct pendingProduct = initPendingProduct(supplierNo,supplierId, supplierName);
		/**
		 * 消息头
		 */
		Map<String,String> headers = new HashMap<String,String>();	
		supplierSaveAndSendToPending(supplierId, supplierName, hubSpu, hubSkus, pendingProduct, headers,supplierPicture); 
		sendPending(supplierName,supplierNo,pendingProduct,headers);
	}
	
	/**
	 * biondioni供应商保存数据以及发送消息给Pending
	 * @param supplierNo 供应商编号
	 * @param supplierId 供应商门户编号
	 * @param supplierName
	 * @param hubSpu
	 * @param hubSkus
	 * @param supplierPicture 图片消息体
	 */
	public void biondioniSaveAndSendToPending(String supplierNo,String supplierId,String supplierName,HubSupplierSpuDto hubSpu,List<HubSupplierSkuDto> hubSkus,SupplierPicture supplierPicture) throws EpHubSupplierProductConsumerException{
		/**
		 * 消息体
		 */
		PendingProduct pendingProduct = initPendingProduct(supplierNo,supplierId, supplierName);
		/**
		 * 消息头
		 */
		Map<String,String> headers = new HashMap<String,String>();	
		supplierSaveAndSendToPending(supplierId, supplierName, hubSpu, hubSkus, pendingProduct, headers,supplierPicture); 
		sendPending(supplierName,supplierNo,pendingProduct,headers); 
	}	
	/**
	 * geb供应商保存数据以及发送消息给Pending
	 * @param supplierNo 供应商编号
	 * @param supplierId
	 * @param supplierName
	 * @param hubSpu
	 * @param hubSkus
	 * @param supplierPicture 图片消息体
	 */
	public void gebSaveAndSendToPending(String supplierNo,String supplierId,String supplierName,HubSupplierSpuDto hubSpu,List<HubSupplierSkuDto> hubSkus,SupplierPicture supplierPicture) throws EpHubSupplierProductConsumerException{
		/**
		 * 消息体
		 */
		PendingProduct pendingProduct = initPendingProduct(supplierNo,supplierId, supplierName);
		/**
		 * 消息头
		 */
		Map<String,String> headers = new HashMap<String,String>();	
		supplierSaveAndSendToPending(supplierId, supplierName, hubSpu, hubSkus, pendingProduct, headers,supplierPicture); 
		sendPending(supplierName,supplierNo,pendingProduct,headers);
	}
	/**
	 * stefania供应商保存数据以及发送消息给Pending
	 * @param supplierNo 供应商编号
	 * @param supplierId
	 * @param supplierName
	 * @param hubSpu
	 * @param hubSkus
	 * @param supplierPicture 图片消息体
	 */
	public void stefaniaSaveAndSendToPending(String supplierNo,String supplierId,String supplierName,HubSupplierSpuDto hubSpu,List<HubSupplierSkuDto> hubSkus,SupplierPicture supplierPicture) throws EpHubSupplierProductConsumerException{
		/**
		 * 消息体
		 */
		PendingProduct pendingProduct = initPendingProduct(supplierNo,supplierId, supplierName);
		/**
		 * 消息头
		 */
		Map<String,String> headers = new HashMap<String,String>();	
		supplierSaveAndSendToPending(supplierId, supplierName, hubSpu, hubSkus, pendingProduct, headers,supplierPicture); 
		sendPending(supplierName,supplierNo,pendingProduct,headers);
	}
	/**
	 * tony供应商保存数据以及发送消息给Pending
	 * @param supplierNo 供应商编号
	 * @param supplierId
	 * @param supplierName
	 * @param hubSpu
	 * @param hubSkus
	 * @param supplierPicture 图片消息体
	 */
	public void tonySaveAndSendToPending(String supplierNo,String supplierId,String supplierName,HubSupplierSpuDto hubSpu,List<HubSupplierSkuDto> hubSkus,SupplierPicture supplierPicture) throws EpHubSupplierProductConsumerException {
		
		PendingProduct pendingProduct = initPendingProduct(supplierNo,supplierId, supplierName);
		Map<String,String> headers = new HashMap<String,String>();	
		supplierSaveAndSendToPending(supplierId, supplierName, hubSpu, hubSkus, pendingProduct, headers,supplierPicture); 
		sendPending(supplierName,supplierNo,pendingProduct,headers);
	}
	/**
	 * coltorti供应商保存数据以及发送消息给Pending
	 * @param supplierNo
	 * @param supplierId
	 * @param supplierName
	 * @param hubSpu
	 * @param hubSkus
	 * @param supplierPicture 图片消息体
	 * @throws EpHubSupplierProductConsumerException
	 */
	public void coltortiSaveAndSendToPending(String supplierNo,String supplierId,String supplierName,HubSupplierSpuDto hubSpu,List<HubSupplierSkuDto> hubSkus,SupplierPicture supplierPicture) throws EpHubSupplierProductConsumerException{
		/**
		 * 消息体
		 */
		PendingProduct pendingProduct = initPendingProduct(supplierNo,supplierId, supplierName);
		/**
		 * 消息头
		 */
		Map<String,String> headers = new HashMap<String,String>();	
		supplierSaveAndSendToPending(supplierId, supplierName, hubSpu, hubSkus, pendingProduct, headers,supplierPicture); 
		sendPending(supplierName,supplierNo,pendingProduct,headers);
	}
	
	
	
	/**
	 * 保存hubSpu以及hubSku，并且构造消息体和消息头
	 * @param supplierId 供应商门户id
	 * @param supplierName 供应商名称
	 * @param hubSpu HubSupplierSpuDto对象
	 * @param hubSkus HubSupplierSkuDto对象集合
	 * @param pendingProduct 消息体对象
	 * @param headers 消息头
	 * @param supplierPicture 图片的消息体
	 */
	public void supplierSaveAndSendToPending(String supplierId,String supplierName,HubSupplierSpuDto hubSpu,List<HubSupplierSkuDto> hubSkus,PendingProduct pendingProduct,Map<String,String> headers,SupplierPicture supplierPicture) throws EpHubSupplierProductConsumerException{
		try {
			PendingSpu pendingSpu = new PendingSpu();		
			List<PendingSku> skus = new ArrayList<PendingSku>();
			//保存hubSpu到数据库
			ProductStatus productStatus = supplierProductMysqlService.isHubSpuChanged(hubSpu,pendingSpu);
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
			if(null != supplierPicture){
				supplierPicture.setSupplierSpuId(hubSpu.getSupplierSpuId()); 
				pictureProductService.sendSupplierPicture(supplierPicture, null); 
			}
		} catch (Exception e) {
			throw new EpHubSupplierProductConsumerException(e.getMessage(),e);
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
