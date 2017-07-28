package com.shangpin.supplier.product.consumer.supplier.allstock;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.message.original.body.SupplierStock;
import com.shangpin.supplier.product.consumer.service.stock.SupplierStockService;

@Service
public class StockHandler {

	@Autowired
	private SupplierStockService supplierStockService;
	
	public void handleAllProductStock(SupplierStock message, Map<String, Object> headers){
		supplierStockService.updateStock(message);
	}
}
