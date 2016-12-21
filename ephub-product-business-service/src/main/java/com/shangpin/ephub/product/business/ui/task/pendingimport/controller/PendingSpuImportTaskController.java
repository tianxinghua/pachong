package com.shangpin.ephub.product.business.ui.task.pendingimport.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.ui.task.pendingimport.service.PendingImportTaskService;
import com.shangpin.ephub.product.business.ui.task.spuimport.dto.HubImportTaskListRequestDto;
import com.shangpin.ephub.product.business.ui.task.spuimport.dto.HubImportTaskRequestDto;
import com.shangpin.ephub.product.business.ui.task.spuimport.service.HubImportTaskService;
import com.shangpin.ephub.product.business.ui.task.spuimport.vo.HubTaskProductResponseDTO;
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
	PendingImportTaskService pendingImportTaskService;
	
	@RequestMapping(value = "/import-spu",method = RequestMethod.POST)
    public HubResponse importSpu(@RequestBody HubImportTaskRequestDto dto){
	        	
		try {
			return pendingImportTaskService.uploadFileAndSave(dto);
		} catch (Exception e) {
			return HubResponse.errorResp("上传文件失败，请重新上传");
		}
		
    }
	@RequestMapping(value = "/import-spu-list",method = RequestMethod.POST)
    public HubResponse importSpuList(@RequestBody HubImportTaskListRequestDto dto){
	        	
		try {
			List<HubTaskProductResponseDTO> list = pendingImportTaskService.findHubTaskList(dto);
			return HubResponse.successResp(list);
		} catch (Exception e) {
			return HubResponse.errorResp("获取列表失败");
		}
    }
}
