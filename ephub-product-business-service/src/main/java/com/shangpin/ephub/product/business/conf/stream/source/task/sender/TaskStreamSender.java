package com.shangpin.ephub.product.business.conf.stream.source.task.sender;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.shangpin.ephub.client.message.task.product.body.Task;
import com.shangpin.ephub.product.business.conf.stream.source.task.channel.TaskSource;
/**
 * <p>Title:OrderStreamSender.java </p>
 * <p>Description: 订单流发送器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月23日 下午3:28:09
 */
@EnableBinding({TaskSource.class})
public class TaskStreamSender {
	
	@Autowired
	private TaskSource productImportTaskSource;
	/**
	 * 待处理商品导入任务数据流发送
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean pendingProductImportTaskStream(Task task, Map<String, ?> headers) {
		return productImportTaskSource.pendingProductImportTask().send(MessageBuilder.withPayload(task).copyHeaders(headers).build());
    }
	/**
	 * 待处理商品导入任务数据流发送
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean hubProductImportTaskStream(Task task, Map<String, ?> headers) {
		return productImportTaskSource.hubProductImportTask().send(MessageBuilder.withPayload(task).copyHeaders(headers).build());
    }
    
    /**
	 * 待处理商品导入任务数据流发送
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean productExportTaskStream(Task task, Map<String, ?> headers) {
		return productImportTaskSource.productExportTask().send(MessageBuilder.withPayload(task).copyHeaders(headers).build());
    }

    /**
     * 刷新字典任务数据流发送
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean refreshDicTaskStream(Task task, Map<String, ?> headers) {
    	return productImportTaskSource.refreshDicTask().send(MessageBuilder.withPayload(task).copyHeaders(headers).build());
    }
}
