package com.shangpin.asynchronous.task.consumer.conf.stream.sink.listener;

import com.shangpin.asynchronous.task.consumer.conf.stream.sink.channel.ProductImportTaskSink;
import com.shangpin.asynchronous.task.consumer.conf.stream.sink.channel.SkuSupplierSelectSink;
import com.shangpin.asynchronous.task.consumer.productexport.adapter.ProductExportHandler;
import com.shangpin.asynchronous.task.consumer.productimport.hub.HubProductImportHandler;
import com.shangpin.asynchronous.task.consumer.productimport.pending.PendingProductImportHandler;
import com.shangpin.asynchronous.task.consumer.service.skusupplierselect.SendToScmService;
import com.shangpin.ephub.client.consumer.hubskusuppliermapping.dto.ProductMessageDto;
import com.shangpin.ephub.client.message.task.product.body.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Map;

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
