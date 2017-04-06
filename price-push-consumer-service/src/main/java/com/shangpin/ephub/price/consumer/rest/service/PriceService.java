package com.shangpin.ephub.price.consumer.rest.service;


import com.shangpin.ephub.price.consumer.conf.stream.source.message.ProductPriceDTO;
import com.shangpin.ephub.price.consumer.conf.stream.source.sender.PriceStreamSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>PriceService </p>
 * <p>Description: 价格推送到消息队列服务</p>

 */
@Service
public class PriceService {
	
	@Autowired
	private PriceStreamSender streamSender;
    /**
     * 发送价格消息
     * @param productPriceDTO 价格信息
     * @return 
     */
	public boolean sendMessageToMQ(ProductPriceDTO productPriceDTO) {
		return streamSender.productPriceStream(productPriceDTO , null);
	}

}
