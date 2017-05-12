package com.shangpin.asynchronous.task.consumer.rest.service;


import com.shangpin.asynchronous.task.consumer.conf.stream.source.sender.HubSkuSupplierSelectStreamSender;
import com.shangpin.ephub.client.consumer.hubskusuppliermapping.dto.ProductMessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**

 * <p>Description: 选品推送到消息队列服务</p>

 */
@Service
public class SkuSelectedMappingService {
	
	@Autowired
	private HubSkuSupplierSelectStreamSender streamSender;
    /**
     * 发送价格消息
     * @param productPriceDTO 价格信息
     * @return 
     */
	public boolean sendMessageToMQ(ProductMessageDto productPriceDTO) {
		return streamSender.productPriceStream(productPriceDTO , null);
	}

}
