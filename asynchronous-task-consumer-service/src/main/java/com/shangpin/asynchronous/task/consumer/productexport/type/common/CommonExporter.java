package com.shangpin.asynchronous.task.consumer.productexport.type.common;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.shangpin.asynchronous.task.consumer.productexport.type.IExportService;
import com.shangpin.ephub.client.message.task.product.body.Task;
/**
 * <p>Title: CommonExporter</p>
 * <p>Description: 导出操作通用业务处理 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月7日 下午2:02:37
 *
 */
@Service
public abstract class CommonExporter implements IExportService {
	
	/**
	 * 获取表格头行
	 * @return
	 */
	public abstract String[] getExcelHeader();

	@Override
	public void productExportTask(Task message, Map<String, Object> headers) {
		//TODO 
		
	}
}
