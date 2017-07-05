package com.shangpin.ephub.product.business.ui.task.pending.export.service;

import com.shangpin.ephub.client.data.mysql.enumeration.TaskType;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskDto;

/**
 * <p>Title: ExportService</p>
 * <p>Description: 导出service接口定义 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月5日 上午11:43:44
 *
 */
public interface ExportService {

	/**
	 * 创建并保存任务
	 * @param createusr 任务的创建人
	 * @param remotePath 导入的文件在ftp上保存的目录
	 * @param taskType 任务类型
	 * @return
	 */
	public HubSpuImportTaskDto createAndSaveTaskIntoMysql(String createusr ,String remotePath ,TaskType taskType);
	/**
	 * 将任务发送到队列
	 * @param taskNo 任务编号
	 * @param taskType 任务类型
	 * @param t 附加的数据
	 * @return
	 */
	public <T> boolean sendTaskToQueue(String taskNo, TaskType taskType, T t);
}
