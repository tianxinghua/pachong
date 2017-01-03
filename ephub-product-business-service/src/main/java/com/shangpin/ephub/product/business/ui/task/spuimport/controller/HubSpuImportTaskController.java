package com.shangpin.ephub.product.business.ui.task.spuimport.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.enumeration.TaskImportTpye;
import com.shangpin.ephub.product.business.ui.task.common.service.TaskImportService;
import com.shangpin.ephub.product.business.ui.task.spuimport.dto.HubImportTaskListRequestDto;
import com.shangpin.ephub.product.business.ui.task.spuimport.dto.HubImportTaskRequestDto;
import com.shangpin.ephub.product.business.ui.task.spuimport.vo.HubTaskProductResponseWithPageDTO;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;


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
@Slf4j
public class HubSpuImportTaskController {
	
	@Autowired
	TaskImportService taskService;
	
	@RequestMapping(value = "/import-spu",method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
	@ResponseBody
    public HubResponse importSpu(@RequestBody HubImportTaskRequestDto dto){
	        	
		try {
			log.info("hub导入接受到的参数："+dto.toString());
			return taskService.uploadFileAndSave(dto,TaskImportTpye.HUB_PRODUCT.getIndex());
		} catch (Exception e) {
			return HubResponse.errorResp("上传文件失败，请重新上传");
		}
    }
	
	@RequestMapping(value = "/import-product-list",method = RequestMethod.POST)
	@ResponseBody
    public HubResponse importSpuList(@RequestBody HubImportTaskListRequestDto dto){
	        	
		log.info("hub任务列表接受到的参数："+dto.toString());
		try {
			Byte [] list1 = {3};
			HubTaskProductResponseWithPageDTO hubTaskProductResponseWithPageDTO = taskService.findHubTaskList(dto,Arrays.asList(list1));
			return HubResponse.successResp(hubTaskProductResponseWithPageDTO);	
		} catch (Exception e) {
			return HubResponse.errorResp("获取列表分页失败");
		}
    }
}
