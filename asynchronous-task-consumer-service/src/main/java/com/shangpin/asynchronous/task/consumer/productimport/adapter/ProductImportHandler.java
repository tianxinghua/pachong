package com.shangpin.asynchronous.task.consumer.productimport.adapter;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.asynchronous.task.consumer.productimport.airshop.service.AirshopImportService;
import com.shangpin.asynchronous.task.consumer.productimport.common.service.TaskImportService;
import com.shangpin.asynchronous.task.consumer.productimport.pending.sku.service.PendingSkuImportService;
import com.shangpin.asynchronous.task.consumer.productimport.pending.spu.service.PendingBrandImportService;
import com.shangpin.asynchronous.task.consumer.productimport.pending.spu.service.PendingCateGroyImportService;
import com.shangpin.asynchronous.task.consumer.productimport.pending.spu.service.PendingColorImportService;
import com.shangpin.asynchronous.task.consumer.productimport.pending.spu.service.PendingMadeImportService;
import com.shangpin.asynchronous.task.consumer.productimport.pending.spu.service.PendingMaterialImportService;
import com.shangpin.asynchronous.task.consumer.productimport.pending.spu.service.PendingSpuImportService;
import com.shangpin.asynchronous.task.consumer.productimport.slot.service.SlotSpuImportService;
import com.shangpin.asynchronous.task.consumer.productimport.supplier.service.SupplierDataImportService;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskType;
import com.shangpin.ephub.client.message.task.product.body.Task;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:ProductImportHandler.java </p>
 * <p>Description: 商品导入处理</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月19日 下午8:03:57
 */
@Component
@Slf4j
public class ProductImportHandler {
	
	@Autowired
	PendingSkuImportService pendingSkuImportService;
	@Autowired
	PendingSpuImportService pendingSpuImportService;
	@Autowired
	PendingColorImportService pendingColorImportService;
	@Autowired
	SlotSpuImportService slotSpuImportService;
	@Autowired 
	TaskImportService taskService;
	@Autowired
	AirshopImportService airshopImportService;
	@Autowired
	PendingCateGroyImportService  CateGroyImportService;
	@Autowired
	PendingMaterialImportService pendingMaterialImportService;
	@Autowired
	PendingMadeImportService pendingMadeImportService;
	@Autowired
	PendingBrandImportService pendingBrandImportService;
	@Autowired
	SupplierDataImportService supplierDataImportService;

	/**
	 * 待处理商品导入数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void pendingImportStreamListen(Task message, Map<String, Object> headers) {
		try {
			
			log.info("导入任务接受到消息：{}",message);
			long start = System.currentTimeMillis();

			//更新任务表，把task_state更新成正在处理
			taskService.updateHubSpuImportByTaskNo(TaskState.HANDLEING.getIndex(), message.getTaskNo(), null,null);
			log.info("任务编号：" + message.getTaskNo() + "状态更新为正在处理");
			
			String resultFile = null;
			if(TaskType.PENDING_SKU.getIndex().equals(message.getType())){
				resultFile = pendingSkuImportService.handMessage(message);
			}else if(TaskType.PENDING_SPU.getIndex().equals(message.getType())){
				resultFile = pendingSpuImportService.handMessage(message);
			}else if(TaskType.IMPORT_WEBSPIDER_SPU.getIndex().equals(message.getType())){
				resultFile = pendingSpuImportService.handMessage(message);
			}else if(TaskType.IMPORT_SLOT_SPU.getIndex().equals(message.getType())){
				resultFile = slotSpuImportService.handMessage(message);
			}else if("18".equals(message.getType())){
				resultFile = airshopImportService.handMessage(message);
			}else if(TaskType.IMPORT_COLOR.getIndex().equals(message.getType())){
				resultFile = pendingColorImportService.handMessage(message);
			}else if(TaskType.IMPORT_ORIGIN.getIndex().equals(message.getType())){
				resultFile = pendingMadeImportService.handMessage(message);
			}else if(TaskType.IMPORT_CATEGORY.getIndex().equals(message.getType())){
				resultFile = CateGroyImportService.handMessage(message);
			}else if(TaskType.IMPORT_MATERIAL.getIndex().equals(message.getType())){
				resultFile = pendingMaterialImportService.handMessage(message);
			}else if(TaskType.IMPORT_BRAND.getIndex().equals(message.getType())){
				resultFile =pendingBrandImportService.handMessage(message);
			}else if(TaskType.SUPPLIER_DATA.getIndex().equals(message.getType())){
				resultFile =supplierDataImportService.handMessage(message);
			}
			
			// 更新结果文件路径到表中
			taskService.updateHubSpuImportByTaskNo(TaskState.ALL_SUCCESS.getIndex(), message.getTaskNo(), null, resultFile);
			log.info("导入任务编号："+message.getTaskNo()+"处理结束，耗时："+(System.currentTimeMillis()-start));
		} catch (Exception e) {
			taskService.updateHubSpuImportByTaskNo(TaskState.SOME_SUCCESS.getIndex(), message.getTaskNo(), "处理任务时发生异常："+e.getMessage(),null);
			log.error("导入任务编号："+message.getTaskNo()+"处理时发生异常",e);
			System.out.println(e.getMessage());
		}
	}

}
