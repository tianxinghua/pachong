package com.shangpin.picture.product.consumer.conf.stream.sink.listener;

import java.util.Map;

import com.shangpin.ephub.client.message.price.body.ProductDTO;
import com.shangpin.picture.product.consumer.conf.stream.sink.adapter.PriceStreamListenerAdapter;
import com.shangpin.picture.product.consumer.conf.stream.sink.channel.PriceSink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.picture.product.consumer.conf.stream.sink.adapter.PictureProductStreamListenerAdapter;
import com.shangpin.picture.product.consumer.conf.stream.sink.channel.PictureProductSink;

/**
  价格消息队列监听
 */
@EnableBinding({PictureProductSink.class})
public class PriceStreamListener {
	
	@Autowired
	private PriceStreamListenerAdapter priceStreamListenerAdapter;
	
	@StreamListener(PriceSink.SUPPLIER_PRICE)
    public void priceStreamListener(@Payload ProductDTO message, @Headers Map<String,Object> headers) throws Exception  {
	//	priceStreamListenerAdapter.supplierPictureProductStreamListen(message,headers);
    }
}
