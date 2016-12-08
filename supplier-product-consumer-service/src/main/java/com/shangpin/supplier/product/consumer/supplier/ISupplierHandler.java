package com.shangpin.supplier.product.consumer.supplier;

import java.util.Map;

import com.shangpin.supplier.product.consumer.conf.stream.sink.message.SupplierProduct;

/**
 * <p>Title:ISupplierHandler </p>
 * <p>Description: 定义一个供应商原始数据处理器接口，统一方法用</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月8日 下午8:42:35
 *
 */
public interface ISupplierHandler {

	/**
	 * @param message 接收到的消息体
	 * @param headers 接收到的消息头
	 */
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers);
}
