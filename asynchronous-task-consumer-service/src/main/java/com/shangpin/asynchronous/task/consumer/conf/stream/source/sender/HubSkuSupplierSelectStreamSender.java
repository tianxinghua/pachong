package com.shangpin.asynchronous.task.consumer.conf.stream.source.sender;


import com.shangpin.asynchronous.task.consumer.conf.stream.source.channel.HubSkuSupplierSelectSource;
import com.shangpin.ephub.client.consumer.hubskusuppliermapping.dto.ProductMessageDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Map;

/**

 * <p>Description: HUB_SKU 选择供货商商品 发送器</p>

 */
@EnableBinding({HubSkuSupplierSelectSource.class})
public class HubSkuSupplierSelectStreamSender {
	
	@Autowired
	private HubSkuSupplierSelectSource skuSupplierSelectSource;
	
	/**
	 *
	 * @param headers 
	 * @param productPriceDTO 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean productPriceStream(ProductMessageDto productPriceDTO, Map<String, ?> headers) {
    	return skuSupplierSelectSource.supplierSkuSelectedProducer().send(MessageBuilder.withPayload(productPriceDTO).copyHeaders(headers).build());
    }
}
