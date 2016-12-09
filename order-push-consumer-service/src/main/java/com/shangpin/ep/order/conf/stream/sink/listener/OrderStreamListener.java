package com.shangpin.ep.order.conf.stream.sink.listener;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ep.order.module.exception.service.IHubOrderExceptionService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import com.shangpin.ep.order.conf.stream.sink.channel.OrderSink;
import com.shangpin.ep.order.conf.stream.sink.message.SupplierOrderSync;
import com.shangpin.ep.order.push.OrderPushManager;

import lombok.extern.slf4j.Slf4j;

/**
 * 订单流处理配置
 * <p>Title:StreamConf.java </p>
 * <p>Description: 订单流数据处理</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月9日 下午6:07:52
 */
@EnableBinding({OrderSink.class})
@Slf4j
public class OrderStreamListener {



	/**
	 * 原队列
	 */
	public static final String ORIGINAL_QUEUE = "HA.SupplierOrderSync.push-order";
	/**
	 * 
	 */
	public static final String DELAY_EXCHANGE = "HA.SupplierOrderSync.Delay";
	/**
	 * 系统DLQ
	 */
	private static final String DLQ = ORIGINAL_QUEUE + ".dlq";
	@Autowired
	private OrderPushManager orderPushManager;

	@Autowired
	private IHubOrderExceptionService hubOrderExceptionService;
	/**
	 * 订单流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OrderSink.ORDERS)
    public void orderStreamListen(@Payload SupplierOrderSync message, @Headers Map<String,Object> headers) throws Exception  {
		orderPushManager.orderProcess(message, headers);
    }
	@Bean
    public DirectExchange delayExchange() {
        DirectExchange exchange = new DirectExchange(DELAY_EXCHANGE);
        exchange.setDelayed(true);
        return exchange;
    }
	@Bean
	public Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
		return new Jackson2JsonMessageConverter();
	}
    @Bean
    public Binding bindOriginalToDelay() {
        return BindingBuilder.bind(new Queue(ORIGINAL_QUEUE)).to(delayExchange()).with(ORIGINAL_QUEUE);
    }
	/**
	 * 系统错误消息处理机制配置
	 * @param failedMessage 系统处理时发生异常的消息
	 */
	@RabbitListener(queues = DLQ)
	public void error(Message failedMessage) {
		MessageProperties properties = failedMessage.getMessageProperties();
		String reason = (String)properties.getHeaders().get("x-exception-message");
		byte[] body = failedMessage.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			SupplierOrderSync supplierOrderSync = objectMapper.readValue(body,SupplierOrderSync.class);
			hubOrderExceptionService.saveHubOrderException(supplierOrderSync,reason);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(" 错误队列反序列化消息体： " +  e.getMessage());
		}
		log.error("ep-order-service系统检测到异常消息："+ failedMessage);
    }
}
