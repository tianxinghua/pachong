package com.shangpin.ephub.price.consumer.conf.stream.sink.listener;

import java.util.Map;


import com.shangpin.ephub.price.consumer.conf.stream.sink.channel.PriceSink;
import com.shangpin.ephub.price.consumer.conf.stream.sink.adapter.PriceStreamListenerAdapter;
import com.shangpin.ephub.price.consumer.conf.stream.source.message.ProductPriceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

/**
  价格消息队列监听
 */
@EnableBinding({PriceSink.class})
public class PriceStreamListener {
	
	@Autowired
	private PriceStreamListenerAdapter priceStreamListenerAdapter;
	
	@StreamListener(PriceSink.SUPPLIER_PRICE)
    public void priceStreamListener(@Payload ProductPriceDTO message, @Headers Map<String,Object> headers) throws Exception  {
		priceStreamListenerAdapter.supplierPriceStreamListen(message,headers);
    }
}
