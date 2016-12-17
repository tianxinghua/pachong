package com.shangpin.ephub.product.business.hubPage.task.spuimport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.hubPage.task.spuimport.dto.HubSpuImportTask;
import com.shangpin.ephub.product.business.hubPage.task.spuimport.service.TaskService;
import com.shangpin.ephub.response.ResponseContent;


/**
 * <p>HubSpuImportTaskController </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月17日 下午5:25:30
 */
@RestController
@RequestMapping("/hubImportTask")
public class HubSpuImportTaskController {
	
	@Autowired
	TaskService taskService;
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/importSpu",method = RequestMethod.POST)
    public ResponseContent countByCriteria(@RequestBody HubSpuImportTask dto){
	        	
		boolean flag = taskService.uploadFileAndSave(dto);
		if(flag){
			return ResponseContent.successResp(null);
		}else{
			return ResponseContent.errorResp("上传文件失败，请重新上传");
		}
		
    }
	
}
