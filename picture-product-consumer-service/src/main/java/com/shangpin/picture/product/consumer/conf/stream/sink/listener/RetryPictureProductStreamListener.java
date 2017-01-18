package com.shangpin.picture.product.consumer.conf.stream.sink.listener;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import com.shangpin.picture.product.consumer.conf.stream.sink.adapter.PictureProductStreamListenerAdapter;
import com.shangpin.picture.product.consumer.conf.stream.sink.channel.RetryPictureProductSink;
import com.shangpin.picture.product.consumer.conf.stream.source.message.RetryPicture;

/**
 * <p>Title:PictureProductStreamSender.java </p>
 * <p>Description: 商品图片数据流发送器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月12日 下午7:47:52
 */
@EnableBinding({RetryPictureProductSink.class})
public class RetryPictureProductStreamListener {
	
	@Autowired
	private PictureProductStreamListenerAdapter pictureProductStreamListenerAdapter;
	
	@StreamListener(RetryPictureProductSink.SUPPLIER_PICTURE)
    public void supplierPictureProductStreamListen(@Payload RetryPicture message, @Headers Map<String,Object> headers) throws Exception  {
		pictureProductStreamListenerAdapter.supplierRetryPictureProductStreamListen(message,headers);
    }
}
