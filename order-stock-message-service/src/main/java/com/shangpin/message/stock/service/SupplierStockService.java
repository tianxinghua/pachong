package com.shangpin.message.stock.service;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.message.conf.mq.MQConf.Module;
import com.shangpin.message.stock.bean.SupplierStockSync;

/**
 *库存同步消息发送器
 *@Project: scm-message-service
 *@Author: yanxiaobin
 *@Date: 2016年10月25日
 *@Copyright: 2016 www.shangpin.com Inc. All rights reserved.
 */
@Component
public class SupplierStockService {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	/**
	 * send Message To Broker 
	 * @param message to be sended
	 * @param priority 
	 */
	public void sendMessageToRabbitMQ(SupplierStockSync message , Integer priority) {
		rabbitTemplate.convertAndSend(Module.STOCK.getExchange(), Module.STOCK.getKey(), message, new MessagePostProcessor() {
			@Override
			public Message postProcessMessage(Message message) throws AmqpException {
				message.getMessageProperties().setPriority(priority);
				return message;
			}
		});
	}

}