package com.shangpin.asynchronous.task.consumer.conf.stream.sink.listener;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import com.shangpin.asynchronous.task.consumer.conf.stream.sink.channel.SkuSupplierSelectSink;
import com.shangpin.asynchronous.task.consumer.service.skusupplierselect.SendToScmService;
import com.shangpin.ephub.client.consumer.hubskusuppliermapping.dto.ProductMessageDto;

/**

 */
@EnableBinding({SkuSupplierSelectSink.class})
public class SkuSupplierSelectStreamListener {
	
	@Autowired
	private SendToScmService sendToScmService;

	/**
	 * pending导入处理
	 * @param message
	 * @param headers
	 * @throws Exception
	 */
	@StreamListener(SkuSupplierSelectSink.SKU_SUPPLIER_SELECTED)
    public void skuSupplierSelectStreamListener(@Payload ProductMessageDto message, @Headers Map<String,Object> headers) throws Exception  {
		sendToScmService.handleDate(message,headers);
    }

}
