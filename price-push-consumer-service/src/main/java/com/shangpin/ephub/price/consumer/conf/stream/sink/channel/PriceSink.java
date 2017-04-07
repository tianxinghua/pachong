package com.shangpin.ephub.price.consumer.conf.stream.sink.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * <p>Title:PictureProductSource.java </p>
 * <p>Description: 价格通道配置</p>

 */
public interface PriceSink {

	public String SUPPLIER_PRICE = "supplierPrice";
	
	/**
	 * @return 供应商商品通用价格数据流通道组件
	 */
	@Input(value = PriceSink.SUPPLIER_PRICE)
    public SubscribableChannel supplierPrice();
}
