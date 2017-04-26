package com.shangpin.ephub.price.consumer.conf.stream.sink.adapter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ephub.price.consumer.conf.stream.source.message.ProductPriceDTO;
import com.shangpin.ephub.price.consumer.processor.SupplierProductPriceProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>Title:PriceStreamListenerAdapter.java </p>
   价格监听适配器

 */
@Component
@Slf4j
public class PriceStreamListenerAdapter {
	
	@Autowired
	private SupplierProductPriceProcessor supplierProductPriceProcessor;

	ObjectMapper mapper = new ObjectMapper();
	/**
	 * 监听供应商原始商品价格数据流
	 * @param message 消息体
	 * @param headers 消息头 请注意，避免修改此消息头，否则将会抛出运行时异常。
	 */
	public void supplierPriceStreamListen(ProductPriceDTO message, Map<String, Object> headers) {
		String messagebody  ="";
		try {
			messagebody = mapper.writeValueAsString(message);
			log.info(" ProductPriceDTO Message body : " + mapper.writeValueAsString(message));
			supplierProductPriceProcessor.processProductPrice(message,headers);
		} catch (Exception e) {

			log.error("   handle ProductPriceDTO body :" + messagebody + "fail,  reason : " +e.getMessage(),e);
		}
	}
	/**
	 * 监听重试拉取的价格
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void supplierRetryPriceStreamListen(ProductPriceDTO message, Map<String, Object> headers) {

		String messagebody  ="";
		try {
			messagebody = mapper.writeValueAsString(message);
			log.info(" ProductPriceDTO Message body in retry mq: " + mapper.writeValueAsString(message));
			supplierProductPriceProcessor.processRetryPrice(message,headers);
		} catch (Exception e) {

			log.error("   handle ProductPriceDTO body :" + messagebody + "fail,  reason : " +e.getMessage(),e);
		}
	}

}
