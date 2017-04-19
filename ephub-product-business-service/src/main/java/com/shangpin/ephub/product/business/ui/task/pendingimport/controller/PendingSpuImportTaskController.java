package com.shangpin.ephub.product.business.ui.task.pendingimport.controller;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.enumeration.TaskType;
import com.shangpin.ephub.client.message.task.product.body.Task;
import com.shangpin.ephub.product.business.common.util.DateTimeUtil;
import com.shangpin.ephub.product.business.conf.stream.source.task.sender.TaskStreamSender;
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
@RequestMapping("/pending-task")
@Slf4j
public class PendingSpuImportTaskController {
	@Autowired
	TaskStreamSender productImportTaskStreamSender;
	@Autowired
	TaskImportService pendingImportTaskService;
	
	@RequestMapping(value = "/import-spu",method = RequestMethod.POST)
    public HubResponse importSpu(@RequestBody HubImportTaskRequestDto dto){
	        	
		try {
			log.info("pendingSpu上传参数：{}",dto);
			return pendingImportTaskService.uploadFileAndSave(dto,TaskType.PENDING_SPU);
		} catch (Exception e) {
			log.error("pendingSpu上传文件失败",e);
			e.printStackTrace();
			return HubResponse.errorResp("上传文件失败，请重新上传");
		}
    }
	
	@RequestMapping(value = "/import-sku",method = RequestMethod.POST)
    public HubResponse importSku(@RequestBody HubImportTaskRequestDto dto){
	        	
		try {
			log.info("pendingSku上传参数：{}",dto);
			return pendingImportTaskService.uploadFileAndSave(dto,TaskType.PENDING_SKU);
		} catch (Exception e) {
			return HubResponse.errorResp("上传文件失败，请重新上传");
		}
    }
	
	@RequestMapping(value = "/import-task-list",method = RequestMethod.POST)
    public HubResponse importSpuList(@RequestBody HubImportTaskListRequestDto dto){
	        	
		try {
			log.info("pending列表参数：{}",dto);
			HubTaskProductResponseWithPageDTO hubTaskProductResponseWithPageDTO = pendingImportTaskService.findHubTaskList(dto);
			return HubResponse.successResp(hubTaskProductResponseWithPageDTO);
		} catch (Exception e) {
			return HubResponse.errorResp("获取列表失败");
		}
    }
	@RequestMapping(value = "/test",method = RequestMethod.POST)
	public HubResponse test(){
		  Task productImportTask = new Task();
	        productImportTask.setMessageDate(DateTimeUtil.getTime(new Date()));
	        productImportTask.setMessageId(UUID.randomUUID().toString());
	        productImportTask.setTaskNo("201701101207");
	        productImportTask.setType(4);
	        productImportTask.setData("{\"taskFtpFilePath\":\"11\"}");
	        log.info("推送任务的参数：{}",productImportTask);
	        productImportTaskStreamSender.productExportTaskStream(productImportTask, null);
	        return HubResponse.successResp(null);
	}
	
}
