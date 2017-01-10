package com.shangpin.asynchronous.task.consumer.productexport.adapter;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.shangpin.asynchronous.task.consumer.productexport.pending.service.ExportServiceImpl;
import com.shangpin.asynchronous.task.consumer.productexport.pending.vo.PendingProducts;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskImportTpye;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;
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
	
	/**
	 * 商品导出数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void productExportTask(ProductImportTask message, Map<String, Object> headers) {
		if(!StringUtils.isEmpty(message.getData())){
			PendingProducts products = JsonUtil.deserialize(message.getData(), PendingProducts.class);
			if(message.getType() == TaskImportTpye.EXPORT_PENDING_SKU.getIndex()){
				exportServiceImpl.exportSku(message.getTaskNo(),products);
			}else if(message.getType() == TaskImportTpye.EXPORT_PENDING_SPU.getIndex()){
				exportServiceImpl.exportSpu(message.getTaskNo(),products); 
			}
		}else{
			log.error("待处理页导出请传入参数！！！"); 
		}
	}

}
