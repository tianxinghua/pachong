package com.shangpin.ephub.product.business.ui.task.spuimport.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.ui.task.common.enumeration.TaskImportTpye;
import com.shangpin.ephub.product.business.ui.task.common.service.TaskImportService;
import com.shangpin.ephub.product.business.ui.task.spuimport.dto.HubImportTaskListRequestDto;
import com.shangpin.ephub.product.business.ui.task.spuimport.dto.HubImportTaskRequestDto;
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
@RequestMapping("/hub-task")
public class HubSpuImportTaskController {
	
	@Autowired
	TaskImportService taskService;
	
	@RequestMapping(value = "/import-spu",method = RequestMethod.POST)
//	@ResponseBody
    public HubResponse importSpu(@RequestBody HubImportTaskRequestDto dto){
	        	
		try {
			return taskService.uploadFileAndSave(dto,TaskImportTpye.HUB_PRODUCT.getIndex());
		} catch (Exception e) {
			return HubResponse.errorResp("上传文件失败，请重新上传");
		}
    }
	@RequestMapping(value = "/down-import-result",method = RequestMethod.POST)
//	@ResponseBody
    public HubResponse<byte[]> downResult(String dto){
	        	
		try {
			return taskService.downResultFile(dto);
		} catch (Exception e) {
			return HubResponse.errorResp("下载失败，请重新上传");
		}
    }
	
	@RequestMapping(value = "/import-spu-list",method = RequestMethod.POST)
    public HubResponse importSpuList(@RequestBody HubImportTaskListRequestDto dto){
	        	
		try {
			Byte [] list1 = {3};
			List<HubTaskProductResponseDTO> list = taskService.findHubTaskList(dto,Arrays.asList(list1));
			return HubResponse.successResp(list);
		} catch (Exception e) {
			return HubResponse.errorResp("获取列表失败");
		}
    }
}
