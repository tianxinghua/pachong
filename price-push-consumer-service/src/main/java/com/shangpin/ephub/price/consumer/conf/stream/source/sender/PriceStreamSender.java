package com.shangpin.ephub.price.consumer.conf.stream.source.sender;


import com.shangpin.ephub.price.consumer.conf.stream.source.channel.PriceSource;
import com.shangpin.ephub.price.consumer.conf.stream.source.message.ProductPriceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Map;

/**

 * <p>Description: 商品价格数据流发送器</p>

 */
@EnableBinding({PriceSource.class})
public class PriceStreamSender {
	
	@Autowired
	private PriceSource priceSource;
	
	/**
	 *
	 * @param headers 
	 * @param productPriceDTO 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean productPriceStream(ProductPriceDTO productPriceDTO, Map<String, ?> headers) {
    	return priceSource.supplierProducerPrice().send(MessageBuilder.withPayload(productPriceDTO).copyHeaders(headers).build());
    }
}
