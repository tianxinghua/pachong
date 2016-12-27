package com.shangpin.asynchronous.task.consumer.productimport.pending;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.asynchronous.task.consumer.productimport.pending.sku.service.PendingSkuImportService;
import com.shangpin.asynchronous.task.consumer.productimport.pending.spu.service.PendingSpuImportService;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskImportTpye;
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
	/**
	 * 待处理商品导入数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void pendingImportStreamListen(ProductImportTask message, Map<String, Object> headers) {
		try {
			log.info("pending任务接受到消息：{}",message);
			String index = (String)headers.get(TaskImportTpye.PENDING_SKU.getIndex());
			if(headers.get(TaskImportTpye.PENDING_SKU.getIndex())!=null){
				PendingSkuImportService.handMessage(message);
			}else if(headers.get(TaskImportTpye.PENDING_SPU.getIndex())!=null){
				PendingSpuImportService.handMessage(message);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
