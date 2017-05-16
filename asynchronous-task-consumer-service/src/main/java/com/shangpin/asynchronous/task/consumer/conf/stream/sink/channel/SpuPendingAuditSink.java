package com.shangpin.asynchronous.task.consumer.conf.stream.sink.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
    spu pending 审核
 */
public interface SpuPendingAuditSink {
	
	public String SPU_PENDING_AUDIT = "pendingAuditConsumer";
	

	
    /**
     *
     * @return 通道
     */
	@Input(value = SpuPendingAuditSink.SPU_PENDING_AUDIT)
    public SubscribableChannel pendingAuditConsumer();

	
}
