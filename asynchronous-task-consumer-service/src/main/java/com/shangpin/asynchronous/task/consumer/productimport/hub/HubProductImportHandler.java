package com.shangpin.asynchronous.task.consumer.productimport.hub;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.asynchronous.task.consumer.productimport.common.service.TaskImportService;
import com.shangpin.asynchronous.task.consumer.productimport.hub.service.HubProductImportService;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:HubProductImportHandler.java </p>
 * <p>Description: hub商品导入处理</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月19日 下午8:03:31
 */
@Component
@Slf4j
public class HubProductImportHandler {
	
	@Autowired
	HubProductImportService hubProductImportService;
	@Autowired
	TaskImportService taskService;
	/**
	 * hub商品导入数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void hubProductImportStreamListen(ProductImportTask message, Map<String, Object> headers) {
		
		try {
			long start = System.currentTimeMillis();
			log.info("接受到消息：{}",message);
			// 1、更新任务表，把task_state更新成正在处理
			taskService.updateHubSpuImportByTaskNo(TaskState.HANDLEING.getIndex(), message.getTaskNo(), null,null);
			log.info("任务编号：" + message.getTaskNo() + "状态更新成正在处理");
			String resultFile = hubProductImportService.handMessage(message);
			
			// 更新结果文件路径到表中
			taskService.updateHubSpuImportByTaskNo(TaskState.ALL_SUCCESS.getIndex(), message.getTaskNo(), null, resultFile);
			log.info("pending任务编号："+message.getTaskNo()+"处理结束，耗时："+(System.currentTimeMillis()-start));
		} catch (Exception e) {
			taskService.updateHubSpuImportByTaskNo(TaskState.SOME_SUCCESS.getIndex(), message.getTaskNo(), "处理任务时发生异常："+e.getMessage(),null);
			log.error("pending任务编号："+message.getTaskNo()+"处理时发生异常",e);
			e.printStackTrace();
		}
	}

}
