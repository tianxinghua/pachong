package com.shangpin.asynchronous.task.consumer.conf.stream.source.sender;


import com.shangpin.asynchronous.task.consumer.conf.stream.source.channel.HubSkuSupplierSelectSource;
import com.shangpin.asynchronous.task.consumer.conf.stream.source.channel.SpuPendingAuditSource;
import com.shangpin.ephub.client.consumer.hubskusuppliermapping.dto.ProductMessageDto;
import com.shangpin.ephub.client.data.mysql.product.dto.SpuModelDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Map;

/**

 * <p>Description: HUB_SKU 选择供货商商品 发送器</p>

 */
@EnableBinding({SpuPendingAuditSource.class})
public class SpuPendingAuditStreamSender {
	
	@Autowired
	private SpuPendingAuditSource spuPendingAuditSource;
	
	/**
	 *
	 * @param headers 
	 * @param spuModelDto 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean sendSpuAuditStream(SpuModelDto spuModelDto, Map<String, ?> headers) {
    	return spuPendingAuditSource.pendingAuditProducer().send(MessageBuilder.withPayload(spuModelDto).copyHeaders(headers).build());
    }
}
