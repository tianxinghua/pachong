package com.shangpin.supplier.product.consumer.supplier.stefania;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.shangpin.supplier.product.consumer.conf.stream.sink.message.SupplierProduct;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;

/**
 * <p>Title:StefaniaHandler.java </p>
 * <p>Description: stefania供货商原始数据处理器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月8日 上午11:36:22
 */
@Component("stefaniaHandler")
public class StefaniaHandler implements ISupplierHandler{
	
	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}

}
