package com.shangpin.asynchronous.task.consumer.productexport.type;

import java.util.Map;

import com.shangpin.ephub.client.message.task.product.body.Task;

/**
 * <p>Title: IExportService</p>
 * <p>Description: 所有导出任务处理规范 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月7日 下午12:21:47
 *
 */
public interface IExportService {

	/**
	 * 处理到处任务，并将导出的文件上传指定位置
	 * @param message
	 * @param headers
	 */
	public void productExportTask(Task message, Map<String, Object> headers) throws Exception ;
}
