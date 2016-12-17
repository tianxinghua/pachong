package com.shangpin.supplier.product.consumer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.pending.body.PendingProduct;
import com.shangpin.ephub.client.message.pending.body.sku.PendingSku;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;
import com.shangpin.ephub.client.message.pending.header.MessageHeaderKey;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.conf.stream.source.sender.PendingProductStreamSender;
import com.shangpin.supplier.product.consumer.enumeration.ProductStatus;
import com.shangpin.supplier.product.consumer.service.dto.Sku;
import com.shangpin.supplier.product.consumer.service.dto.Spu;
/**
 * <p>Title:SupplierProductSaveAndSendToPending </p>
 * <p>Description: 各个供应商保存数据并且发消息给Pending</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月15日 下午2:23:47
 *
 */
@Component
public class SupplierProductSaveAndSendToPending {

	@Autowired
	private SupplierProductMysqlService supplierProductMysqlService;
	@Autowired
	private PendingProductStreamSender pendingProductStreamSender;
		
	/**
	 * atelier系列供应商保存数据以及发送消息给Pending
	 * @param supplierId
	 * @param supplierName
	 * @param hubSpu
	 * @param hubSkus
	 */
	public void atelierSaveAndSendToPending(String supplierId,String supplierName,HubSupplierSpuDto hubSpu,List<HubSupplierSkuDto> hubSkus){
		/**
		 * 消息体
		 */
		PendingProduct pendingProduct = initPendingProduct(supplierId, supplierName);
		/**
		 * 消息头
		 */
		Map<String,String> headers = new HashMap<String,String>();	
		supplierSaveAndSendToPending(supplierId, supplierName, hubSpu, hubSkus, pendingProduct, headers); 
		if("2015091801507".equals(supplierId)){
			pendingProductStreamSender.brunarossoPendingProductStream(pendingProduct, headers);
		}else if("2015082701461".equals(supplierId)){
			pendingProductStreamSender.ostorePendingProductStream(pendingProduct, headers);
		}
		
	}
	/**
	 * spinnaker系列供应商保存数据以及发送消息给Pending
	 * @param supplierId
	 * @param supplierName
	 * @param hubSpu
	 * @param hubSkus
	 */
	public void spinnakerSaveAndSendToPending(String supplierId,String supplierName,HubSupplierSpuDto hubSpu,List<HubSupplierSkuDto> hubSkus){
		/**
		 * 消息体
		 */
		PendingProduct pendingProduct = initPendingProduct(supplierId, supplierName);
		/**
		 * 消息头
		 */
		Map<String,String> headers = new HashMap<String,String>();	
		supplierSaveAndSendToPending(supplierId, supplierName, hubSpu, hubSkus, pendingProduct, headers); 
		if("2015081701439".equals(supplierId)){
			pendingProductStreamSender.spinnakerPendingProductStream(pendingProduct, headers);
		}
		
	}
	
	/**
	 * biondioni供应商保存数据以及发送消息给Pending
	 * @param supplierId
	 * @param supplierName
	 * @param hubSpu
	 * @param hubSkus
	 */
	public void biondioniSaveAndSendToPending(String supplierId,String supplierName,HubSupplierSpuDto hubSpu,List<HubSupplierSkuDto> hubSkus){
		/**
		 * 消息体
		 */
		PendingProduct pendingProduct = initPendingProduct(supplierId, supplierName);
		/**
		 * 消息头
		 */
		Map<String,String> headers = new HashMap<String,String>();	
		supplierSaveAndSendToPending(supplierId, supplierName, hubSpu, hubSkus, pendingProduct, headers); 
		pendingProductStreamSender.biondioniPendingProductStream(pendingProduct, headers);		
	}	
	/**
	 * geb供应商保存数据以及发送消息给Pending
	 * @param supplierId
	 * @param supplierName
	 * @param hubSpu
	 * @param hubSkus
	 */
	public void gebSaveAndSendToPending(String supplierId,String supplierName,HubSupplierSpuDto hubSpu,List<HubSupplierSkuDto> hubSkus){
		/**
		 * 消息体
		 */
		PendingProduct pendingProduct = initPendingProduct(supplierId, supplierName);
		/**
		 * 消息头
		 */
		Map<String,String> headers = new HashMap<String,String>();	
		supplierSaveAndSendToPending(supplierId, supplierName, hubSpu, hubSkus, pendingProduct, headers); 
		pendingProductStreamSender.gebPendingProductStream(pendingProduct, headers);
	}
	/**
	 * stefania供应商保存数据以及发送消息给Pending
	 * @param supplierId
	 * @param supplierName
	 * @param hubSpu
	 * @param hubSkus
	 */
	public void stefaniaSaveAndSendToPending(String supplierId,String supplierName,HubSupplierSpuDto hubSpu,List<HubSupplierSkuDto> hubSkus){
		/**
		 * 消息体
		 */
		PendingProduct pendingProduct = initPendingProduct(supplierId, supplierName);
		/**
		 * 消息头
		 */
		Map<String,String> headers = new HashMap<String,String>();	
		supplierSaveAndSendToPending(supplierId, supplierName, hubSpu, hubSkus, pendingProduct, headers); 
		pendingProductStreamSender.stefaniaPendingProductStream(pendingProduct, headers);
	}
	/**
	 * tony供应商保存数据以及发送消息给Pending
	 * @param supplierId
	 * @param supplierName
	 * @param hubSpu
	 * @param hubSkus
	 */
	public void tonySaveAndSendToPending(String supplierId,String supplierName,HubSupplierSpuDto hubSpu,List<HubSupplierSkuDto> hubSkus){
		/**
		 * 消息体
		 */
		PendingProduct pendingProduct = initPendingProduct(supplierId, supplierName);
		/**
		 * 消息头
		 */
		Map<String,String> headers = new HashMap<String,String>();	
		supplierSaveAndSendToPending(supplierId, supplierName, hubSpu, hubSkus, pendingProduct, headers); 
		pendingProductStreamSender.tonyPendingProductStream(pendingProduct, headers);
	}
	
	
	
	/**
	 * 保存hubSpu以及hubSku，并且构造消息体和消息头
	 * @param supplierId 供应商门户id
	 * @param supplierName 供应商名称
	 * @param hubSpu HubSupplierSpuDto对象
	 * @param hubSkus HubSupplierSkuDto对象集合
	 * @param pendingProduct 消息体对象
	 * @param headers 消息头
	 */
	public void supplierSaveAndSendToPending(String supplierId,String supplierName,HubSupplierSpuDto hubSpu,List<HubSupplierSkuDto> hubSkus,PendingProduct pendingProduct,Map<String,String> headers){
		
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
					ProductStatus skuStatus = supplierProductMysqlService.isHubSkuChanged(hubSku, pendingSku);
					skus.add(pendingSku);
					headSku.setSupplierId(supplierId);
					headSku.setSkuNo(hubSku.getSupplierSkuNo());
					headSku.setStatus(skuStatus.getIndex());
					headSkus.add(headSku);
				} catch (Exception e) {
					// TODO: handle exception
				}				
			}
		}		
		pendingSpu.setSkus(skus);
		pendingProduct.setData(pendingSpu);		
		spuHead.setSkus(headSkus);		
		headers.put(MessageHeaderKey.PENDING_PRODUCT_MESSAGE_HEADER_KEY, JsonUtil.serialize(spuHead));
	}
	
	
	/**
	 * 赋值Spu并返回
	 * @param supplierId
	 * @param supplierSpuNo
	 * @param productStatus
	 * @return
	 */
	private Spu setSpuHead(String supplierId,String supplierSpuNo,int productStatus){
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
	private PendingProduct initPendingProduct(String supplierId, String supplierName) {
		PendingProduct pendingProduct = new PendingProduct();
		pendingProduct.setSupplierId(supplierId);
		pendingProduct.setSupplierName(supplierName);
		return pendingProduct;
	}
}
