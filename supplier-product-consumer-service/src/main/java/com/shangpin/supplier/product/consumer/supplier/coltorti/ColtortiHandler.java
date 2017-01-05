package com.shangpin.supplier.product.consumer.supplier.coltorti;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;

@Component("coltortiHandler")
public class ColtortiHandler implements ISupplierHandler {

	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
//		String data = message.getMessageDate();
//		System.out.println(data); 
	}

}
