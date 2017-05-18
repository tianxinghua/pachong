package com.shangpin.asynchronous.task.consumer.conf.stream.source.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 *  </p>

 */
public interface SpuPendingAuditSource {

	public String SPU_PENDING_AUDIT = "pendingAuditProducer";
	
	/**
	 * @return HUB_SPU_PENDING 审核 生产渠道
	 */
	@Output(value = SpuPendingAuditSource.SPU_PENDING_AUDIT)
    public MessageChannel pendingAuditProducer();
}
