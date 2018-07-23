package com.shangpin.supplier.product.consumer.conf.stream.sink.listener;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import com.shangpin.ephub.client.message.task.product.body.Task;
import com.shangpin.supplier.product.consumer.conf.stream.sink.channel.TaskSink;
import com.shangpin.supplier.product.consumer.refreshDic.RefreshDicStreamListenerAdapter;

/**
 * <p>Title:ProductImportTaskStreamListener.java </p>
 * <p>Description: 商品导入处理监听器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月19日 下午7:56:23
 */
@EnableBinding({TaskSink.class})
public class TaskStreamListener {
	
	@Autowired
	private RefreshDicStreamListenerAdapter refreshDicStreamListenerAdapter;
	/**
	 * 待处理商品导入任务数据流发送
	 * @param message 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
	@StreamListener(TaskSink.REFRESH_DIC)
    public void productStreamListen(@Payload Task message, @Headers Map<String,Object> headers) throws Exception  {
		refreshDicStreamListenerAdapter.refreshDicTask(message,headers);
    }
}
