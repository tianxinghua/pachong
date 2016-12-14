package com.shangpin.supplier.product.consumer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.supplier.product.consumer.conf.client.mysql.sku.bean.HubSupplierSku;
import com.shangpin.supplier.product.consumer.conf.client.mysql.spu.bean.HubSupplierSpu;

@Component
public class SupplierProductSaveAndSendToPending {

	@Autowired
	private SupplierProductMysqlService supplierProductMysqlService;
	@Autowired
	private SupplierProductSendService supplierProductSendService;
	
	public void saveAndSendToPending(HubSupplierSpu hubSpu,List<HubSupplierSku> hubSkus){
		//TODO 查找对比hub spu主要信息
		//1.查不到
		//2.查到 2.1未变化 2.2改变了
		//TODO 查找对比hub sku价格信息
		//1.查不到
		//2.查到 2.1未变化 2.2 改变
		
		
	}
}
