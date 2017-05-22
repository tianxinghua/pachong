package com.shangpin.asynchronous.task.consumer.rest.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.asynchronous.task.consumer.conf.stream.source.sender.SpuPendingAuditStreamSender;
import com.shangpin.ephub.client.data.mysql.product.dto.SpuModelDto;

/**

 * <p>Description: 选品推送到消息队列服务</p>

 */
@Service
public class MessageSendService {
	
	@Autowired
	private SpuPendingAuditStreamSender spuPendingAuditStreamSender;
    /**
     * 发送价格消息
     * @param spuModelDto
     * @return 
     */
	public boolean sendMessageToMQ(SpuModelDto spuModelDto) {
		return spuPendingAuditStreamSender.sendSpuAuditStream(spuModelDto , null);
	}

}
