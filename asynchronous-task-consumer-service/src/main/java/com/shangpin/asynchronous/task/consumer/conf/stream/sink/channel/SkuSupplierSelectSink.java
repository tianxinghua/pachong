package com.shangpin.asynchronous.task.consumer.conf.stream.sink.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
    SKU 选品
 */
public interface SkuSupplierSelectSink {
	
	public String SKU_SUPPLIER_SELECTED = "supplierSkuSelectedConsumer";
	

	
    /**
     *   导入处理
     * @return 通道
     */
	@Input(value = SkuSupplierSelectSink.SKU_SUPPLIER_SELECTED)
    public SubscribableChannel supplierSkuSelectedConsumer();

	
}
