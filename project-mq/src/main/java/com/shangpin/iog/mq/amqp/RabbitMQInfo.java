package com.shangpin.iog.mq.amqp;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/** 
 * @author yanxiaobin E-mail: yanxiaobin@shangpin.com
 * @date 创建时间：2016年5月26日 上午9:51:00 
 * @version 1.0 
 * @since 1.7  
 * RabbitMQ配置参数获取
 */
@Setter
@Getter
@Component("rabbitMqConfig")
@PropertySource("classpath:conf.properties")
public class RabbitMQInfo {

	/**
	 * RabbitMQ host.
	 */
	@Value("${host}")
	private String host;

	/**
	 * RabbitMQ port.
	 */
	@Value("${port}")
	private int port;

	/**
	 * Login user to authenticate to the broker.
	 */
	@Value("${username}")
	private String username;

	/**
	 * Login to authenticate against the broker.
	 */
	@Value("${password}")
	private String password;
	/**
	 * Virtual host to use when connecting to the broker.
	 */
	@Value("${virtualHost}")
	private String virtualHost;
	
	/**
	 * 消费者线程数（应设置为18的倍数，18个队列）
	 */
	@Value("${concurrentConsumerNums}")
	private int concurrentConsumerNums ;

	
}
