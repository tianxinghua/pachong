package com.shangpin.supplier.product.consumer.supplier.ostore;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.supplier.product.consumer.conf.stream.sink.message.SupplierProduct;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.AtelierCommonHandler;

/**
 * * 
 * <p>Title:OstoreHandler </p>
 * <p>Description: ostore供应商原始数据处理器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月8日 下午4:00:05
 *
 */
@Component("ostoreHandler")
public class OstoreHandler implements ISupplierHandler {
	
	@Autowired
	private AtelierCommonHandler atelierCommonHandler;

	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		
		message.getData();
	}

	
}
