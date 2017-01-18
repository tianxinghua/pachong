package com.shangpin.picture.product.consumer.conf.stream.source.sender;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.shangpin.picture.product.consumer.conf.stream.source.channel.RetryPictureProductSource;
import com.shangpin.picture.product.consumer.conf.stream.source.message.RetryPicture;

/**
 * <p>Title:PictureProductStreamSender.java </p>
 * <p>Description: 商品图片数据流发送器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月12日 下午7:47:52
 */
@EnableBinding({RetryPictureProductSource.class})
public class RetryPictureProductStreamSender {
	
	@Autowired
	private RetryPictureProductSource pictureRetryProductSource;
	
	/**
	 * 发送供应商biondioni商品流数据
	 * @param headers 
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean supplierPictureProductStream(RetryPicture retryPicture, Map<String, ?> headers) {
    	return pictureRetryProductSource.supplierRetryPictureProduct().send(MessageBuilder.withPayload(retryPicture).copyHeaders(headers).build());
    }
}
