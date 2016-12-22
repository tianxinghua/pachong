package com.shangpin.asynchronous.task.consumer.productimport.hub;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.asynchronous.task.consumer.productimport.hub.service.HubProductImportService;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;

/**
 * <p>Title:HubProductImportHandler.java </p>
 * <p>Description: hub商品导入处理</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月19日 下午8:03:31
 */
@Component
public class HubProductImportHandler {
	
	@Autowired
	HubProductImportService hubProductImportService;
	
	/**
	 * hub商品导入数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void hubProductImportStreamListen(ProductImportTask message, Map<String, Object> headers) {
		
		try {
			hubProductImportService.handMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
