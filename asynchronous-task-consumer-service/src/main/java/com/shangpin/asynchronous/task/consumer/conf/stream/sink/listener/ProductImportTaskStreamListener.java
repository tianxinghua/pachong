package com.shangpin.asynchronous.task.consumer.conf.stream.sink.listener;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import com.shangpin.asynchronous.task.consumer.conf.stream.sink.channel.ProductImportTaskSink;
import com.shangpin.asynchronous.task.consumer.productimport.hub.HubProductImportHandler;
import com.shangpin.asynchronous.task.consumer.productimport.pending.PendingProductImportHandler;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;

/**
 * <p>Title:ProductImportTaskStreamListener.java </p>
 * <p>Description: 商品导入处理监听器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月19日 下午7:56:23
 */
@EnableBinding({ProductImportTaskSink.class})
public class ProductImportTaskStreamListener {
	
	@Autowired
	private PendingProductImportHandler pendingProductImportHandler;
	@Autowired
	private HubProductImportHandler hubProductImportHandler;
	/**
	 * pending导入处理
	 * @param message
	 * @param headers
	 * @throws Exception
	 */
	@StreamListener(ProductImportTaskSink.PENDING_IMPORT)
    public void biondioniPendingProductStreamListen(@Payload ProductImportTask message, @Headers Map<String,Object> headers) throws Exception  {
		pendingProductImportHandler.pendingImportStreamListen(message,headers);
    }
	/**
	 * hub导入处理
	 * @param message
	 * @param headers
	 * @throws Exception
	 */
	@StreamListener(ProductImportTaskSink.HUB_IMPORT)
    public void brunarossoPendingProductStreamListen(@Payload ProductImportTask message, @Headers Map<String,Object> headers) throws Exception  {
		hubProductImportHandler.hubProductImportStreamListen(message,headers);
    }
}
