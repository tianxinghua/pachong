package com.shangpin.supplier.product.consumer.conf.stream.sink.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
/**
 * <p>Title:ProductTaskSink.java </p>
 * <p>Description: 商品任务</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月19日 下午7:48:12
 */
public interface TaskSink {
	
	public String REFRESH_DIC = "refreshDicTask";
	
	/**
	 * 导出处理
	 * @return 通道
	 */
	@Input(value = TaskSink.REFRESH_DIC)
    public SubscribableChannel productExport();
	
}
