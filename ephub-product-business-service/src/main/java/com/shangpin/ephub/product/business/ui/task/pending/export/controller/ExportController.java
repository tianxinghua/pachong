package com.shangpin.ephub.product.business.ui.task.pending.export.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**
 * <p>Title: ExportController</p>
 * <p>Description: 待处理页（待拍照）的导出 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月5日 上午11:21:42
 *
 */

import com.shangpin.ephub.client.data.mysql.enumeration.TaskType;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.service.IPendingProductService;
import com.shangpin.ephub.product.business.ui.task.pending.export.service.ExportService;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;
@RestController
@RequestMapping("/pending-export")
@Slf4j
public class ExportController {
	
	@Autowired
	private ExportService exportService;
	@Autowired
	private IPendingProductService pendingProductService;

	@RequestMapping(value="/wait-to-shoot",method=RequestMethod.POST)
	public HubResponse<?> exportWaitToShoot(@RequestBody PendingQuryDto pendingQuryDto){
		try {
			String remotePath = "pending_export";
			//第一步创建任务
			HubSpuImportTaskDto task = exportService.createAndSaveTaskIntoMysql(pendingQuryDto.getCreateUser(), remotePath , TaskType.EXPORT_WAIT_SHOOT);
			//第二步发送队列
			pendingQuryDto.setShoot(true);
			int total = pendingProductService.countByPendingQury(pendingQuryDto);
			pendingQuryDto.setPageSize(total); 
			boolean bool = exportService.sendTaskToQueue(task.getTaskNo(), TaskType.EXPORT_WAIT_SHOOT, pendingQuryDto);
			if(bool){
				return HubResponse.successResp(task.getTaskNo()+":"+task.getSysFileName());
			}
		} catch (Exception e) {
			log.error("待拍照导出异常："+e.getMessage(),e);
		}
		return HubResponse.errorResp("待拍照导出异常");
	}
}
