package com.shangpin.supplier.product.consumer.supplier.common.spinnaker;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.shangpin.supplier.product.consumer.conf.client.mysql.sku.bean.HubSupplierSku;
import com.shangpin.supplier.product.consumer.conf.client.mysql.spu.bean.HubSupplierSpu;
import com.shangpin.supplier.product.consumer.conf.stream.sink.message.SupplierProduct;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.spinnaker.dto.Sku;
import com.shangpin.supplier.product.consumer.supplier.common.spinnaker.dto.Spu;

/**
 * <p>Title:ISpinnakerHandler </p>
 * <p>Description: 规范spinnaker供应商对接的抽象类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月10日 下午2:38:43
 *
 */
@Component
public abstract class ISpinnakerHandler implements ISupplierHandler {
	
	/**
	 * 将原始对象转换成hub对象
	 * @param supplierId 供应商门户编号
	 * @param spu 原始spu对象
	 * @param sku 原始sku对象
	 * @param hubSpu hub spu
	 * @return
	 */
	public abstract boolean convertSpu(String supplierId, Spu spu, Sku sku, HubSupplierSpu hubSpu);
	
	/**
	 * 将原始对象转换成hub对象
	 * @param supplierId 供应商门户编号
	 * @param supplierSpuId bub spu编号
	 * @param sku 原始sku对象
	 * @param hubSku hub sku
	 * @return
	 */
	public abstract boolean convertSku(String supplierId, Long supplierSpuId, Sku sku, HubSupplierSku hubSku);

	/**
	 * spinnaker通用处理主流程
	 * @param message
	 * @param headers
	 */
	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		if(!StringUtils.isEmpty(message.getData())){
			Spu spu = new Gson().fromJson(message.getData(), Spu.class);			
			if(null != spu.getItems() && null != spu.getItems().getItem() && spu.getItems().getItem().size()>0){
				for(Sku sku : spu.getItems().getItem()){
					HubSupplierSpu hubSpu =  new HubSupplierSpu();
					boolean success = convertSpu(message.getSupplierId(),spu,sku,hubSpu);
					if(success){
						//TODO 保存hubSpu
					}
					HubSupplierSku hubSku = new HubSupplierSku();
					boolean skuSucc = convertSku(message.getSupplierId(),hubSpu.getSupplierSpuId(),sku,hubSku);
					if(skuSucc){
						//TODO 保存hubSku
					}
				}
			}
		}
		
	}

}
