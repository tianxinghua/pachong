package com.shangpin.picture.product.consumer.conf.stream.sink.adapter;

import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.picture.product.consumer.conf.stream.source.message.RetryPicture;
import com.shangpin.picture.product.consumer.processor.SupplierProductPictureProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>Title:PriceStreamListenerAdapter.java </p>
   价格监听适配器

 */
@Component
public class PriceStreamListenerAdapter {
	
	@Autowired
	private SupplierProductPictureProcessor supplierProductPictureProcessor;
	/**
	 * 监听供应商原始商品图片数据流
	 * @param message 消息体
	 * @param headers 消息头 请注意，避免修改此消息头，否则将会抛出运行时异常。
	 */
	public void supplierPictureProductStreamListen(SupplierPicture message, Map<String, Object> headers) {
		supplierProductPictureProcessor.processProductPicture(message,headers);
	}
	/**
	 * 监听重试拉取图片
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void supplierRetryPictureProductStreamListen(RetryPicture message, Map<String, Object> headers) {
		supplierProductPictureProcessor.processRetryProductPicture(message, headers);
	}

}
