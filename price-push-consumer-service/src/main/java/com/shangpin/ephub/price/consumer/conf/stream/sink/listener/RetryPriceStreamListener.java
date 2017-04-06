package com.shangpin.ephub.price.consumer.conf.stream.sink.listener;


import com.shangpin.ephub.price.consumer.conf.stream.sink.adapter.PriceStreamListenerAdapter;
import com.shangpin.ephub.price.consumer.conf.stream.sink.channel.RetryPriceSink;
import com.shangpin.ephub.price.consumer.conf.stream.source.message.ProductPriceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Map;

/**
 * <p>Title:PictureProductStreamSender.java </p>
 * <p>Description: 商品价格数据流发送器</p>

 */
@EnableBinding({RetryPriceSink.class})
public class RetryPriceStreamListener {
	
	@Autowired
	private PriceStreamListenerAdapter priceStreamListenerAdapter;
	
	@StreamListener(RetryPriceSink.SUPPLIER_PRICE)
    public void supplierPriceStreamListen(@Payload ProductPriceDTO message, @Headers Map<String,Object> headers) throws Exception  {
		priceStreamListenerAdapter.supplierRetryPriceStreamListen(message,headers);
    }
}
