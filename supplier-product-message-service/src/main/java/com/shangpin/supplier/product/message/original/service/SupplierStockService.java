package com.shangpin.supplier.product.message.original.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.message.original.body.SupplierStock;
import com.shangpin.supplier.product.message.original.conf.sender.OriginalProductStreamSender;

import lombok.extern.slf4j.Slf4j;
/**
 *
 * <p>Title: SupplierStockService</p>
 * <p>Description: 更新ephub库存的服务 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月24日 下午3:44:15
 *
 */
@Service
@Slf4j
public class SupplierStockService {
	
	@Autowired
	private OriginalProductStreamSender sender;
	
	public boolean sendMessageToChannel(SupplierStock supplierStock){
		try {
			return sender.allProductStockStream(supplierStock);
		} catch (Exception e) {
			log.error("更新库存异常，supplierId："+supplierStock.getSupplierId()+"supplierSkuNo："+supplierStock.getSupplierSkuNo()+" EXCEPTION："+e.getMessage()); 
		}
		return false;
	}
}
