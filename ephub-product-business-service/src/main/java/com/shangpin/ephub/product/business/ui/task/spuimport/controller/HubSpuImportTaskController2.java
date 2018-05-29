package com.shangpin.ephub.product.business.ui.task.spuimport.controller;

import com.shangpin.ephub.client.data.mysql.enumeration.TaskType;
import com.shangpin.ephub.product.business.ui.task.common.service.TaskImportService;
import com.shangpin.ephub.product.business.ui.task.spuimport.dto.HubImportTaskListRequestDto;
import com.shangpin.ephub.product.business.ui.task.spuimport.dto.HubImportTaskRequestDto;
import com.shangpin.ephub.product.business.ui.task.spuimport.vo.HubTaskProductResponseWithPageDTO;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>HubSpuImportTaskController </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月17日 下午5:25:30
 */
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/color-task")
@Slf4j
public class HubSpuImportTaskController2 {
	
	@Autowired
	TaskImportService taskService;
	
	@RequestMapping(value = "/import-color",method = RequestMethod.POST)
	@ResponseBody
    public HubResponse importSpu(@RequestBody HubImportTaskRequestDto dto){
	        	
		try {
			log.info("hub导入接受到的参数："+dto.toString());
			return taskService.uploadFileAndSave(dto,TaskType.IMPORT_COLOR);
		} catch (Exception e) {
			return HubResponse.errorResp("上传文件失败，请重新上传");
		}
    }
	
	@RequestMapping(value = "/import-product-list",method = RequestMethod.POST)
	@ResponseBody
    public HubResponse importSpuList(@RequestBody HubImportTaskListRequestDto dto){
	        	
		log.info("hub任务列表接受到的参数："+dto.toString());
		try {
			HubTaskProductResponseWithPageDTO hubTaskProductResponseWithPageDTO = taskService.findHubTaskList(dto);
			return HubResponse.successResp(hubTaskProductResponseWithPageDTO);	
		} catch (Exception e) {
			return HubResponse.errorResp("获取列表分页失败");
		}
    }
}
