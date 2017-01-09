package com.shangpin.asynchronous.task.consumer.productexport;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;

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
	
	/**
	 * 商品导出数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */

	public void productExportTask(ProductImportTask message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}

}
