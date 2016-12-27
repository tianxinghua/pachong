package com.shangpin.ephub.product.business.ui.task.pendingimport.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.enumeration.TaskImportTpye;
import com.shangpin.ephub.product.business.ui.task.common.service.TaskImportService;
import com.shangpin.ephub.product.business.ui.task.spuimport.dto.HubImportTaskListRequestDto;
import com.shangpin.ephub.product.business.ui.task.spuimport.dto.HubImportTaskRequestDto;
import com.shangpin.ephub.product.business.ui.task.spuimport.vo.HubTaskProductResponseWithPageDTO;
import com.shangpin.ephub.response.HubResponse;


/**
 * <p>HubSpuImportTaskController </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月17日 下午5:25:30
 */
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/pending-task")
public class PendingSpuImportTaskController {
	
	@Autowired
	TaskImportService pendingImportTaskService;
	
	@RequestMapping(value = "/import-spu",method = RequestMethod.POST)
    public HubResponse importSpu(@RequestBody HubImportTaskRequestDto dto){
	        	
		try {
			return pendingImportTaskService.uploadFileAndSave(dto,TaskImportTpye.PENDING_SPU.getIndex());
		} catch (Exception e) {
			return HubResponse.errorResp("上传文件失败，请重新上传");
		}
    }
	
	@RequestMapping(value = "/import-sku",method = RequestMethod.POST)
    public HubResponse importSku(@RequestBody HubImportTaskRequestDto dto){
	        	
		try {
			return pendingImportTaskService.uploadFileAndSave(dto,TaskImportTpye.PENDING_SKU.getIndex());
		} catch (Exception e) {
			return HubResponse.errorResp("上传文件失败，请重新上传");
		}
    }
	
	@RequestMapping(value = "/import-task-list",method = RequestMethod.POST)
    public HubResponse importSpuList(@RequestBody HubImportTaskListRequestDto dto){
	        	
		try {
			Byte [] list1 = {1,2};
			HubTaskProductResponseWithPageDTO hubTaskProductResponseWithPageDTO = pendingImportTaskService.findHubTaskList(dto,Arrays.asList(list1));
			return HubResponse.successResp(hubTaskProductResponseWithPageDTO);
		} catch (Exception e) {
			return HubResponse.errorResp("获取列表失败");
		}
    }
}
