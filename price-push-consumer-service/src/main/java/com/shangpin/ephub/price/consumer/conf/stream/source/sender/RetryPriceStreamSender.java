package com.shangpin.ephub.price.consumer.conf.stream.source.sender;

import com.shangpin.ephub.price.consumer.conf.stream.source.channel.RetryPriceSource;
import com.shangpin.ephub.price.consumer.conf.stream.source.message.ProductPriceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Map;

/**

 * <p>Description: 商品价格数据流发送器</p>

 */
@EnableBinding({RetryPriceSource.class})
public class RetryPriceStreamSender {
	
	@Autowired
	private RetryPriceSource retryPriceSource;
	
	/**
	 * 发送供应商biondioni商品流数据
	 * @param headers 
	 * @param retryPrice 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean supplierPictureProductStream(ProductPriceDTO retryPrice, Map<String, ?> headers) {
    	return retryPriceSource.supplierRetryPrice().send(MessageBuilder.withPayload(retryPrice).copyHeaders(headers).build());
    }
}
