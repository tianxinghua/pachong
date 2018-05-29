package com.shangpin.ephub.product.business.ui.task.pending.export.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskType;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.client.message.task.product.body.Task;
import com.shangpin.ephub.client.util.DateTimeUtil;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.conf.stream.source.task.sender.TaskStreamSender;
import com.shangpin.ephub.product.business.ui.task.pending.export.service.ExportService;
/**
 * <p>Title: ExportServiceImpl</p>
 * <p>Description: 导出service实现类 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月5日 上午11:44:41
 *
 */
@Service
public class ExportServiceImpl implements ExportService {
	
	@Autowired 
	private HubSpuImportTaskGateWay taskGateway;
	@Autowired 
    private TaskStreamSender tastSender;

	@Override
	public HubSpuImportTaskDto createAndSaveTaskIntoMysql(String createusr, String remotePath, TaskType taskType) {
		HubSpuImportTaskDto task = new HubSpuImportTaskDto();
    	Date date = new Date();
		task.setTaskNo(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date));
		task.setTaskState((byte)TaskState.HANDLEING.getIndex());
		task.setCreateTime(date);
		task.setUpdateTime(date);
		task.setImportType(taskType.getIndex().byteValue());
		task.setCreateUser(createusr); 
		String fileName = createusr+"_" + task.getTaskNo()+".xls";
		String filePath = remotePath+File.separator+fileName;

		task.setTaskFtpFilePath(filePath); 
		task.setSysFileName(fileName); 
		task.setResultFilePath(filePath); 
		Long spuImportTaskId = taskGateway.insert(task);
		task.setSpuImportTaskId(spuImportTaskId);
		return task;
	}

	@Override
	public <T> boolean sendTaskToQueue(String taskNo, TaskType taskType, T t) {
		Task productImportTask = new Task();
    	productImportTask.setMessageId(UUID.randomUUID().toString());
    	productImportTask.setTaskNo(taskNo);
    	productImportTask.setMessageDate(DateTimeUtil.getTime(new Date())); 
    	if(null != t){
    		productImportTask.setData(JsonUtil.serialize(t)); 
    	}
    	productImportTask.setType(taskType.getIndex());
    	return tastSender.productExportTaskStream(productImportTask, null);
	}

}
