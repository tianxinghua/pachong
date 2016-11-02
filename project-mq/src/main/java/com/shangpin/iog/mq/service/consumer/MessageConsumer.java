package com.shangpin.iog.mq.service.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.mq.dto.ProductDTO;
import com.shangpin.iog.mq.service.ProductPriceService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 消息处理接收器
 * @author yanxiaobin
 *
 */
@Component
public class MessageConsumer {
	private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger
			.getLogger("info");
	private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger
			.getLogger("error");
	/**
	 *
	 */
	@Autowired
	private ProductPriceService productPriceService;
	

	/**
	 *  消息队列消费者
	 */
	public ProductDTO priceProductQueueConsumer(byte[] bytes) throws Throwable{
		ProductDTO messageBody = null;
		try {
			long start = System.currentTimeMillis();
			ObjectMapper om = new ObjectMapper();
			messageBody = om.readValue(bytes, ProductDTO.class);
			//TODO 调用接口
//			HttpUtil45.post();
			// TODO 修改状态



			long end = System.currentTimeMillis();
			loggerInfo.info("Successfully handling of message 【 "+messageBody+" 】 , and spend time : "+(end-start)+" milliseconds");
		} catch (Exception e) {
			e.printStackTrace();
			loggerError.error("获取内容失败" + e.getMessage(),e);
		}
		return messageBody;
	}


	/**
	 *  消息队列消费者
	 */
	public ProductDTO supplierPriceProductQueueConsumer(byte[] bytes) throws Throwable{
		ProductDTO messageBody = null;
		try {
			long start = System.currentTimeMillis();
			ObjectMapper om = new ObjectMapper();
			messageBody = om.readValue(bytes, ProductDTO.class);
			long end = System.currentTimeMillis();
			loggerInfo.info("Successfully handling of message 【 "+messageBody+" 】 , and spend time : "+(end-start)+" milliseconds");
		} catch (Exception e) {
			e.printStackTrace();
			loggerError.error("获取内容失败" + e.getMessage(),e);
		}
		return messageBody;
	}
	/**
	 * 默认没有消费者的消息接收处理
	 * @throws Exception
	 */
	public void handleMessage(byte[] bytes) throws Exception{
		loggerError.error("UnknowMessage ------> Consumers not found the corresponding queue message , No consumer handling of the message is :"+new String(bytes));
	}
}
