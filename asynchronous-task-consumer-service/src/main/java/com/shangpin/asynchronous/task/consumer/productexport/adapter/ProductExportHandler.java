package com.shangpin.asynchronous.task.consumer.productexport.adapter;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.shangpin.asynchronous.task.consumer.productexport.type.IExportService;
import com.shangpin.asynchronous.task.consumer.productexport.type.allproduct.AllProductServiceImpl;
import com.shangpin.asynchronous.task.consumer.productexport.type.pending.ExportServiceImpl;
import com.shangpin.asynchronous.task.consumer.productimport.common.service.TaskImportService;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskType;
import com.shangpin.ephub.client.data.mysql.spu.dto.PendingQuryDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.SlotManageQuery;
import com.shangpin.ephub.client.message.task.product.body.Task;
import com.shangpin.ephub.client.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:PendingProductImportHandler.java </p>
 * <p>Description: 商品导出处理</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2017年01月09日 下午05:03:57
 */
@Component
@Slf4j
public class ProductExportHandler {
	
	@Autowired
	private ExportServiceImpl exportServiceImpl;
	@Autowired
	TaskImportService taskService;
	@Autowired
	AllProductServiceImpl allProductServiceImpl;
	
	@Autowired
	@Qualifier("waitShootExporter")
	private IExportService waitShootExporter;
	
	@Autowired
	@Qualifier("commitedExporter") 
	private IExportService commitedExporter;
	
	/**
	 * 商品导出数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void productExportTask(Task message, Map<String, Object> headers) {
		try{
			if(!StringUtils.isEmpty(message.getData())){
				
				log.info("接收到导出任务："+message.getData());
				if(message.getType() == TaskType.EXPORT_PENDING_SKU.getIndex() || message.getType() == TaskType.EXPORT_SKU_ALL.getIndex()){
					PendingQuryDto pendingQuryDto = JsonUtil.deserialize(message.getData(), PendingQuryDto.class);
					exportServiceImpl.exportSku(message.getTaskNo(),pendingQuryDto);
				}else if(message.getType() == TaskType.EXPORT_PENDING_SPU.getIndex() || message.getType() == TaskType.EXPORT_SPU_ALL.getIndex()){
					PendingQuryDto pendingQuryDto = JsonUtil.deserialize(message.getData(), PendingQuryDto.class);
					exportServiceImpl.exportSpu(message.getTaskNo(),pendingQuryDto); 
				}else if(message.getType() == TaskType.EXPORT_HUB_SELECTED.getIndex()){
					exportServiceImpl.exportHubSelected(message); 
				}else if(message.getType() == TaskType.EXPORT_HUB_PIC.getIndex()){
					exportServiceImpl.exportHubPicSelected(message); 
				}else if(message.getType() == TaskType.EXPORT_HUB_NOT_HANDLE_PIC.getIndex()){
					exportServiceImpl.exportHubPicSelected2(message); 
				}else if(message.getType() == TaskType.EXPORT_HUB_CHECK_PIC.getIndex()){
					exportServiceImpl.exportHubCheckPicSelected(message); 
				}else if(message.getType() == TaskType.ALL_PRODUCT.getIndex()){
					PendingQuryDto pendingQuryDto = JsonUtil.deserialize(message.getData(), PendingQuryDto.class);
					allProductServiceImpl.exportproductAll(message.getTaskNo(), pendingQuryDto);
				}else if(message.getType() == TaskType.EXPORT_SUTDIO_SLOT.getIndex()){
					SlotManageQuery slotManageQuery = JsonUtil.deserialize(message.getData(), SlotManageQuery.class);
					allProductServiceImpl.exportStudioSlot(message.getTaskNo(), slotManageQuery);
				}else if(message.getType() == TaskType.EXPORT_WAIT_SHOOT.getIndex()){
					waitShootExporter.productExportTask(message, headers); 
				}else if(message.getType() == TaskType.EXPORT_COMMITED.getIndex()){
					commitedExporter.productExportTask(message, headers); 
				}
			}else{
				log.error("待处理页导出请传入参数！！！"); 
			}
		}catch (Exception e) {
            log.error("任务处理异常："+e.getMessage()+"，接收到导出任务："+message.getData(),e);
            taskService.updateHubSpuImportByTaskNo(TaskState.SOME_SUCCESS.getIndex(), message.getTaskNo(), "处理任务时发生异常："+e.getMessage(),null);
        }
		
	}

}
