package com.shangpin.asynchronous.task.consumer.conf.stream.sink.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
/**
 * <p>Title:ProductTaskSink.java </p>
 * <p>Description: 商品任务</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月19日 下午7:48:12
 */
public interface ProductImportTaskSink {
	
	public String PENDING_IMPORT = "pendingProductImportTask";
	
	public String HUB_IMPORT = "hubProductImportTask";
	
    /**
     * pending导入处理
     * @return 通道
     */
	@Input(value = ProductImportTaskSink.PENDING_IMPORT)
    public SubscribableChannel pendingProductImport();
	/**
	 * hub导入处理
	 * @return 通道
	 */
	@Input(value = ProductImportTaskSink.HUB_IMPORT)
    public SubscribableChannel hubProductImport();
}
