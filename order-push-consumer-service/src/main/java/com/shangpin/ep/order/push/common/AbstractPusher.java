package com.shangpin.ep.order.push.common;

import java.util.Map;
import java.util.Map.Entry;

import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import org.apache.commons.collections.MapUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ep.order.conf.stream.sink.listener.OrderStreamListener;
import com.shangpin.ep.order.conf.stream.sink.message.SupplierOrderSync;
import com.shangpin.ep.order.enumeration.OrderStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>Title:AbstractPusher.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月21日 上午2:35:59
 */

public abstract class AbstractPusher {
	
	private static final String PARKING_LOT = "HA.SupplierOrderSync.push-order.dlq";
	public static final String RETRY_PREFIX = "shangpin.hub";
	private int retry;//重试次数

	@Autowired
    private RabbitTemplate rabbitTemplate;

	@Autowired
	SupplierProperties supplierProperties;

	public void reTry(SupplierOrderSync messageBody, Integer delayTime, Integer orderStatus ,Integer pushStatus , Integer retryNum ,Map<String, Object> otherProperties) throws JsonProcessingException {
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setDelay(delayTime);
		if (orderStatus != null) {
			messageProperties.setHeader(GlobalConstant.MESSAGE_HEADER_ORDER_STATUS_KEY, orderStatus);
		}
		if (pushStatus != null) {
			messageProperties.setHeader(GlobalConstant.MESSAGE_HEADER_PUSH_STATUS_KEY, pushStatus);
		}
		if (MapUtils.isNotEmpty(otherProperties)) {
			for (Entry<String, Object> entry : otherProperties.entrySet()) {
				messageProperties.setHeader(entry.getKey(), entry.getValue());
			}
		}

			Message message =  new Message(new ObjectMapper().writeValueAsBytes(messageBody), messageProperties);
			if (retryNum == null) {
				retryNum = Integer.valueOf(0);
			}
			if (retryNum < supplierProperties.getSupplier().getRetry()) {
				messageProperties.setHeader(GlobalConstant.MESSAGE_HEADER_X_RETRIES_KEY, retryNum + 1);
				this.rabbitTemplate.send(OrderStreamListener.DELAY_EXCHANGE, OrderStreamListener.ORIGINAL_QUEUE, message);
			}
			else {
				this.rabbitTemplate.send(PARKING_LOT, message);
			}



    }
}
