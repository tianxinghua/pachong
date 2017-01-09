package com.shangpin.asynchronous.task.consumer.productimport.pending;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.asynchronous.task.consumer.productimport.common.service.TaskImportService;
import com.shangpin.asynchronous.task.consumer.productimport.pending.sku.service.PendingSkuImportService;
import com.shangpin.asynchronous.task.consumer.productimport.pending.spu.service.PendingSpuImportService;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskImportTpye;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:PendingProductImportHandler.java </p>
 * <p>Description: pending商品导入处理</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月19日 下午8:03:57
 */
@Component
@Slf4j
public class PendingProductImportHandler {
	
	@Autowired
	PendingSkuImportService PendingSkuImportService;
	@Autowired
	PendingSpuImportService PendingSpuImportService;
	@Autowired 
	TaskImportService taskService;
	/**
	 * 待处理商品导入数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void pendingImportStreamListen(ProductImportTask message, Map<String, Object> headers) {
		try {
			
			log.info("pending任务接受到消息：{}",message);
			long start = System.currentTimeMillis();

			//更新任务表，把task_state更新成正在处理
			taskService.updateHubSpuImportByTaskNo(TaskState.HANDLEING.getIndex(), message.getTaskNo(), null,null);
			log.info("任务编号：" + message.getTaskNo() + "状态更新为正在处理");
			
			String index = String.valueOf(TaskImportTpye.PENDING_SKU.getIndex());
			if(headers.get(index)!=null){
				PendingSkuImportService.handMessage(message);
			}else{
				PendingSpuImportService.handMessage(message);
			}
			log.info("pending任务编号："+message.getTaskNo()+"处理结束，耗时："+(System.currentTimeMillis()-start));

		} catch (Exception e) {
			taskService.updateHubSpuImportByTaskNo(TaskState.SOME_SUCCESS.getIndex(), message.getTaskNo(), "处理任务时发生异常："+e.getMessage(),null);
			log.error("pending任务编号："+message.getTaskNo()+"处理时发生异常",e);
			e.printStackTrace();
		}
	}

}
