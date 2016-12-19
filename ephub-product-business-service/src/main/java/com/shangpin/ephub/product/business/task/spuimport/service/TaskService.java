package com.shangpin.ephub.product.business.task.spuimport.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shangpin.ephub.client.data.mysql.config.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.client.data.mysql.config.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.product.business.task.spuimport.dto.HubSpuImportTaskParam;
import com.shangpin.ephub.product.business.task.spuimport.util.FTPClientUtil;
import com.shangpin.ephub.response.HubResponse;

/**
 * <p>
 * Title:SupplierOrderService.java
 * </p>
 * <p>
 * Description: task
 * </p>
 * <p>
 * Company: www.shangpin.com
 * </p>
 * 
 * @author zhaogenchun
 * @date 2016年11月23日 下午4:06:52
 */
@SuppressWarnings("rawtypes")
@Service
public class TaskService {

	@Autowired 
	HubSpuImportTaskGateWay spuImportGateway;
	private static String ftpPath = "F://";
	public HubResponse uploadFileAndSave(HubSpuImportTaskParam task) throws Exception{
		
		boolean flag = false;
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date date = new Date();
		String systemFileName = ftpPath+sim.format(date)+task.getFileName().split(".")[1];
		//第一步 ： 上传ftp
		flag = FTPClientUtil.uploadFile(task.getUploadfile(),ftpPath,systemFileName);
		//第二步 ： 保存数据库
		if(flag){
			saveTask(task);
			//第三步 ： 发送到hub消息队列
			
			return HubResponse.successResp(null);
		}else{
			return HubResponse.errorResp("上传ftp失败");
		}
	}
	private boolean saveTask(HubSpuImportTaskParam task) throws Exception{
		// TODO Auto-generated method stub
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		
		HubSpuImportTaskDto hubSpuTask = new HubSpuImportTaskDto();
		Date date = new Date();
		
		hubSpuTask.setTaskNo(sim.format(date));
		hubSpuTask.setTaskFtpFilePath(ftpPath+sim.format(date)+task.getFileName().split(".")[1]);
		hubSpuTask.setSysFileName(sim.format(date)+task.getFileName().split(".")[1]);
	    hubSpuTask.setLocalFileName(task.getFileName());
	    hubSpuTask.setCreateTime(date);
	    //同一个文件上传两次，版本怎么修改
	    hubSpuTask.setVersion(null);
	    hubSpuTask.setCreateUser(task.getCreateUser());
	    spuImportGateway.insert(hubSpuTask);
		return true;
	}

}
