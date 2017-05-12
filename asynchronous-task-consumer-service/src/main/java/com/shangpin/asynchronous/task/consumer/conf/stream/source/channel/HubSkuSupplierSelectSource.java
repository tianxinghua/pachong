package com.shangpin.asynchronous.task.consumer.conf.stream.source.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 *  </p>

 */
public interface HubSkuSupplierSelectSource {

	public String HUB_SKU_SUPPLIER_SELECTED = "supplierSkuSelectedProducer";
	
	/**
	 * @return HUB_SKU 选择供货商商品 生产渠道
	 */
	@Output(value = HubSkuSupplierSelectSource.HUB_SKU_SUPPLIER_SELECTED)
    public MessageChannel supplierSkuSelectedProducer();
}
