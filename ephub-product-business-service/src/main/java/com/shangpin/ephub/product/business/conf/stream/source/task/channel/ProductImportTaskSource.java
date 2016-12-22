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
	
	
	public String PENDING_IMPORT = "pendingProductImportTask";
	
	public String HUB_IMPORT = "hubProductImportTask";
	
    /**
     * pending导入处理
     * @return 通道
     */
	@Output(value = ProductImportTaskSource.PENDING_IMPORT)
    public MessageChannel pendingProductImportTask();
	/**
	 * hub导入处理
	 * @return 通道
	 */
	@Output(value = ProductImportTaskSource.HUB_IMPORT)
    public MessageChannel hubProductImportTask();
}
