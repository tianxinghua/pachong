package com.shangpin.ephub.product.business.ui.task.pendingimport.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.esotericsoftware.minlog.Log;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskImportTpye;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;
import com.shangpin.ephub.product.business.common.util.DateTimeUtil;
import com.shangpin.ephub.product.business.conf.stream.source.task.sender.ProductImportTaskStreamSender;
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
	ProductImportTaskStreamSender productImportTaskStreamSender;
	@Autowired
	TaskImportService pendingImportTaskService;
	
	@RequestMapping(value = "/import-spu",method = RequestMethod.POST)
    public HubResponse importSpu(@RequestBody HubImportTaskRequestDto dto){
	        	
		try {
			log.info("pendingSpu上传参数：{}",dto);
			return pendingImportTaskService.uploadFileAndSave(dto,TaskImportTpye.PENDING_SPU);
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
			return pendingImportTaskService.uploadFileAndSave(dto,TaskImportTpye.PENDING_SKU);
		} catch (Exception e) {
			return HubResponse.errorResp("上传文件失败，请重新上传");
		}
    }
	
	@RequestMapping(value = "/import-task-list",method = RequestMethod.POST)
    public HubResponse importSpuList(@RequestBody HubImportTaskListRequestDto dto){
	        	
		try {
			Byte [] list1 = {1,2};
			log.info("pending列表参数：{}",dto);
			HubTaskProductResponseWithPageDTO hubTaskProductResponseWithPageDTO = pendingImportTaskService.findHubTaskList(dto,Arrays.asList(list1));
			return HubResponse.successResp(hubTaskProductResponseWithPageDTO);
		} catch (Exception e) {
			return HubResponse.errorResp("获取列表失败");
		}
    }
}
