package com.shangpin.ephub.price.consumer.conf.stream.sink.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * <p>Title:PictureProductSource.java </p>
 * <p>Description: 供货商价格通道配置</p>

 */
public interface RetryPriceSink {

	public String SUPPLIER_PRICE = "retrySupplierPrice";
	
	/**
	 * @return 供应商商品通用价格数据流通道组件
	 */
	@Input(value = RetryPriceSink.SUPPLIER_PRICE)
    public SubscribableChannel retrySupplierPrice();
}
