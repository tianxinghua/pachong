package com.shangpin.ephub.product.business.ui.task.dic.seasonExport;

import com.shangpin.ephub.client.data.mysql.enumeration.TaskType;
import com.shangpin.ephub.client.data.mysql.studio.spusupplierunion.dto.SpuSupplierQueryDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuService;
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.service.IPendingProductService;
import com.shangpin.ephub.product.business.ui.task.pending.export.service.ExportService;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Title: ExportController</p>
 * <p>Description: 季节的导出 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月5日 上午11:21:42
 *
 */

@RestController
@RequestMapping("/season-export")
@Slf4j
public class SeasonExportController {
	
	@Autowired
	private ExportService exportService;
	@Autowired
	private IPendingProductService pendingProductService;
	@Autowired
    private HubSlotSpuService slotSpuService;

	/**
	 * 待拍照导出
	 * @param
	 * @return
	 */
	@RequestMapping(value="/season-dic-export",method=RequestMethod.POST)
	public HubResponse<?> exportWaitToShoot(@RequestBody SeasonMappingDto seasonMappingDto){
		try {
			String remotePath = "pending_export";
			//第一步创建任务
			HubSpuImportTaskDto task = exportService.createAndSaveTaskIntoMysql(seasonMappingDto.getCreateUser(), remotePath , TaskType.EXPORT_CATEGORY);
			//第二步发送队列
			//int total = pendingProductService.countByPendingQury(seasonMappingDto);
			//seasonMappingDto.setPageSize(total);
			boolean bool = exportService.sendTaskToQueue(task.getTaskNo(), TaskType.EXPORT_WAIT_SHOOT, seasonMappingDto);
			if(bool){
				return HubResponse.successResp(task.getTaskNo()+":"+task.getSysFileName());
			}
		} catch (Exception e) {
			log.error("待拍照导出异常："+e.getMessage(),e);
		}
		return HubResponse.errorResp("待拍照导出异常");
	}
	
	/**
	 * 已提交导出
	 * @param quryDto
	 * @return
	 */
	@RequestMapping(value="/commited",method=RequestMethod.POST)
	public HubResponse<?> exportCommited(@RequestBody SpuSupplierQueryDto quryDto){
		try {
			String remotePath = "pending_export";
			//第一步创建任务
			HubSpuImportTaskDto task = exportService.createAndSaveTaskIntoMysql(quryDto.getCreateUser(), remotePath , TaskType.EXPORT_COMMITED);
			//第二步发送队列
			quryDto.setPageSize(slotSpuService.countSlotSpu(quryDto));
			boolean bool = exportService.sendTaskToQueue(task.getTaskNo(), TaskType.EXPORT_COMMITED, quryDto);
			if(bool){
				return HubResponse.successResp(task.getTaskNo()+":"+task.getSysFileName());
			}
		} catch (Exception e) {
			log.error("已提交页面导出异常："+e.getMessage(),e); 
		}
		return HubResponse.errorResp("已提交页面导出异常");
	}
}
