package com.shangpin.supplier.product.consumer.supplier.common.spinnaker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
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
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	
	/**
	 * 将原始对象转换成hub对象
	 * @param supplierId 供应商门户编号
	 * @param spu 原始spu对象
	 * @param sku 原始sku对象
	 * @param hubSpu hub spu
	 * @return
	 */
	public abstract boolean convertSpu(String supplierId, Spu spu, Sku sku, HubSupplierSpuDto hubSpu);
	
	/**
	 * 将原始对象转换成hub对象
	 * @param supplierId 供应商门户编号
	 * @param supplierSpuId bub spu编号
	 * @param sku 原始sku对象
	 * @param hubSku hub sku
	 * @return
	 */
	public abstract boolean convertSku(String supplierId, Long supplierSpuId, Sku sku, HubSupplierSkuDto hubSku);

	/**
	 * spinnaker通用处理主流程
	 * @param message
	 * @param headers
	 */
	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		if(!StringUtils.isEmpty(message.getData())){
			Spu spu = JsonUtil.deserialize(message.getData(), Spu.class);			
			if(null != spu.getItems() && null != spu.getItems().getItem() && spu.getItems().getItem().size()>0){
				for(Sku sku : spu.getItems().getItem()){
					HubSupplierSpuDto hubSpu =  new HubSupplierSpuDto();
					boolean success = convertSpu(message.getSupplierId(),spu,sku,hubSpu);
					HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
					boolean skuSucc = convertSku(message.getSupplierId(),hubSpu.getSupplierSpuId(),sku,hubSku);
					List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
					if(skuSucc){
						hubSkus.add(hubSku);
					}
					if(success){
						supplierProductSaveAndSendToPending.spinnakerSaveAndSendToPending(message.getSupplierId(), message.getSupplierName(), hubSpu, hubSkus);
					}
				}
			}
		}
		
	}

}
