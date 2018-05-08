package com.shangpin.asynchronous.task.consumer.conf.stream.sink.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**

 */
public interface ProductExportTaskSink {
	

	public String PRODUCT_EXPORT = "productExportTask";

	
	/**
	 * 导出处理
	 * @return 通道
	 */
	@Input(value = ProductExportTaskSink.PRODUCT_EXPORT)
    public SubscribableChannel productExport();
	
}
