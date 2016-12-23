package com.shangpin.ephub.product.business.conf.stream.source.task.sender;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;
import com.shangpin.ephub.product.business.conf.stream.source.task.channel.ProductImportTaskSource;
/**
 * <p>Title:OrderStreamSender.java </p>
 * <p>Description: 订单流发送器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月23日 下午3:28:09
 */
@EnableBinding({ProductImportTaskSource.class})
public class ProductImportTaskStreamSender {
	
	@Autowired
	private ProductImportTaskSource productImportTaskSource;
	/**
	 * 待处理商品导入任务数据流发送
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean pendingProductImportTaskStream(ProductImportTask productImportTask, Map<String, ?> headers) {
		return productImportTaskSource.pendingProductImportTask().send(MessageBuilder.withPayload(productImportTask).copyHeaders(headers).build());
    }
	/**
	 * 待处理商品导入任务数据流发送
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean hubProductImportTaskStream(ProductImportTask productImportTask, Map<String, ?> headers) {
		return productImportTaskSource.hubProductImportTask().send(MessageBuilder.withPayload(productImportTask).copyHeaders(headers).build());
    }
}
