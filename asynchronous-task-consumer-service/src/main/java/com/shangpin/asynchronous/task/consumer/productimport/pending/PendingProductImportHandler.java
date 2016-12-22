package com.shangpin.asynchronous.task.consumer.productimport.pending;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.asynchronous.task.consumer.productimport.hub.service.HubProductImportService;
import com.shangpin.asynchronous.task.consumer.productimport.pending.service.PendingProductImportService;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;

/**
 * <p>Title:PendingProductImportHandler.java </p>
 * <p>Description: pending商品导入处理</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月19日 下午8:03:57
 */
@Component
public class PendingProductImportHandler {
	
	@Autowired
	PendingProductImportService pendingProductImportService;
	/**
	 * 待处理商品导入数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void pendingImportStreamListen(ProductImportTask message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		try {
			pendingProductImportService.handMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
