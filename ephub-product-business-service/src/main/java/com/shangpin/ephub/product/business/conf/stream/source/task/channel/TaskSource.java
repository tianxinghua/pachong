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
public interface TaskSource {
	
	
	public String PENDING_IMPORT = "pendingProductImportTask";
	
	public String HUB_IMPORT = "hubProductImportTask";
	public String PRODUCT_EXPORT = "productExportTask";
	public String REFRESH_DIC = "refreshDicTask";
	
    /**
     * pending导入处理
     * @return 通道
     */
	@Output(value = TaskSource.PENDING_IMPORT)
    public MessageChannel pendingProductImportTask();
	/**
	 * hub导入处理
	 * @return 通道
	 */
	@Output(value = TaskSource.HUB_IMPORT)
    public MessageChannel hubProductImportTask();
	
	/**
	 * 导出处理
	 * @return 通道
	 */
	@Output(value = TaskSource.PRODUCT_EXPORT)
    public MessageChannel productExportTask();
	
	/**
	 * 刷新字典
	 * @return 通道
	 */
	@Output(value = TaskSource.REFRESH_DIC)
	public MessageChannel refreshDicTask();
}
