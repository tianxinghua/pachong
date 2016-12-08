package com.shangpin.supplier.product.consumer.supplier.stefania;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.shangpin.supplier.product.consumer.conf.stream.sink.message.SupplierProduct;

/**
 * <p>Title:StefaniaHandler.java </p>
 * <p>Description: stefania供货商原始数据处理器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月8日 上午11:36:22
 */
@Service
public class StefaniaHandler {
	/**
	 * @param message 接收到的消息体
	 * @param headers 接收到的消息头
	 */
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}

}
