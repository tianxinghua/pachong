package com.shangpin.asynchronous.task.consumer.conf.stream.sink.listener;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import com.shangpin.asynchronous.task.consumer.conf.stream.sink.channel.SpuPendingAuditSink;
import com.shangpin.asynchronous.task.consumer.service.pending.SpuPendingAuditService;
import com.shangpin.ephub.client.data.mysql.product.dto.SpuModelDto;

/**
    SPUPENDING 审核处理
 */
@EnableBinding({SpuPendingAuditSink.class})
public class SpuPendingAuditStreamListener {
	
	@Autowired
	private SpuPendingAuditService spuPendingAuditService;

	/**
	 * SPUPENDING 审核 监听
	 * @param message
	 * @param headers
	 * @throws Exception
	 */
	@StreamListener(SpuPendingAuditSink.SPU_PENDING_AUDIT)
    public void spuPendingAuditStreamListener(@Payload SpuModelDto message, @Headers Map<String,Object> headers) throws Exception  {
		spuPendingAuditService.auditSpuPending(message,headers);
    }

}
