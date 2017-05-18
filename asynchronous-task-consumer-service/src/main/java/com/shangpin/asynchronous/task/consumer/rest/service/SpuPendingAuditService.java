package com.shangpin.asynchronous.task.consumer.rest.service;


import com.shangpin.asynchronous.task.consumer.conf.stream.source.sender.HubSkuSupplierSelectStreamSender;
import com.shangpin.asynchronous.task.consumer.conf.stream.source.sender.SpuPendingAuditStreamSender;
import com.shangpin.ephub.client.consumer.hubskusuppliermapping.dto.ProductMessageDto;
import com.shangpin.ephub.client.data.mysql.product.dto.SpuModelDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**

 * <p>Description: 选品推送到消息队列服务</p>

 */
@Service
public class SpuPendingAuditService {
	
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
