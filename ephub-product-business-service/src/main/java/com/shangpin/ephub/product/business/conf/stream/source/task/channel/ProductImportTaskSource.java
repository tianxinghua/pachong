package com.shangpin.ephub.product.business.conf.stream.source.task.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
/**
 * <p>Title:OriginalProductSource.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月5日 下午7:34:44
 */
public interface ProductImportTaskSource {
	
	public String PRODUCT_IMPORT_TASK = "productImportTask";
	
	/**
	 * 商品导入任务通道
	 * @return 消息通道
	 */
	@Output(value = ProductImportTaskSource.PRODUCT_IMPORT_TASK)
    public MessageChannel productImportTask();
}
