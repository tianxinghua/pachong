package com.shangpin.iog.mq.amqp;



import com.shangpin.iog.mq.service.consumer.MessageConsumer;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yanxiaobin E-mail: yanxiaobin@shangpin.com
 * @date 创建时间：2016年5月26日 上午9:54:58
 * @version 1.0
 * @since 1.7
 */
@Configuration
public class RabbitMQConf {

	@Autowired
	private RabbitMQInfo rabbitMqConfig;

	@Autowired
	private MessageConsumer messageConsumer;
	/**
	 * 创建连接工厂
	 * 
	 * @return 连接工厂
	 */
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setAddresses(rabbitMqConfig.getHost());
		connectionFactory.setPort(rabbitMqConfig.getPort());
		connectionFactory.setUsername(rabbitMqConfig.getUsername());
		connectionFactory.setPassword(rabbitMqConfig.getPassword());
		connectionFactory.setVirtualHost(rabbitMqConfig.getVirtualHost());
		return connectionFactory;
	}

	/**
	 * 创建消息转换器，以便以JSON格式将消息发出
	 * 
	 * @return JsonMessageConverter
	 */
	@Bean
	public Jackson2JsonMessageConverter convert() {
		return new Jackson2JsonMessageConverter();
	}




	@Bean
	public SimpleMessageListenerContainer messageListenerContainer() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory());
		container.setQueueNames(Queue.QUEUE_PRODUCT_PRICE_QUEUE.getMessageName(),
				Queue.QUEUE_PRODUCT_SUPPLIER_PRICE_QUEUE.getMessageName());
		container.setConcurrentConsumers(rabbitMqConfig.getConcurrentConsumerNums());
		MessageListenerAdapter messageListener = new MessageListenerAdapter(messageConsumer);
		// messageListener.setMessageConverter(convert());
		messageListener.addQueueOrTagToMethodName(Queue.QUEUE_PRODUCT_PRICE_QUEUE.getMessageName(), "priceProductQueueConsumer");
		messageListener.addQueueOrTagToMethodName(Queue.QUEUE_PRODUCT_SUPPLIER_PRICE_QUEUE.getMessageName(), "supplierPriceProductQueueConsumer");

		container.setMessageListener(messageListener);
		return container;
	}



	@Bean
	public AmqpAdmin amqpAdmin() {



		AmqpAdmin amqpAdmin = new RabbitAdmin(connectionFactory());


		amqpAdmin.declareQueue(myQueue());
		amqpAdmin.declareExchange(tagExchange());
		amqpAdmin.declareBinding(BindingBuilder.bind(myQueue()).to(tagExchange()).with(Queue.QUEUE_PRODUCT_PRICE_ROUTE.getMessageName()));

		return amqpAdmin;
	}

	/**
	 * 创建消息交换器
	 * @return 消息交换器，类型为直接类型交换器
	 */
	@Bean
	public DirectExchange tagExchange(){
		return new DirectExchange(Queue.QUEUE_PRODUCT_PRICE_EXCHANGE.getMessageName(), true, false);
	}




	@Bean
	public org.springframework.amqp.core.Queue myQueue() {
		return new org.springframework.amqp.core.Queue(Queue.QUEUE_PRODUCT_PRICE_QUEUE.getMessageName(),true);
	}

	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate rabbitTemplate =  new RabbitTemplate();
		rabbitTemplate.setConnectionFactory(connectionFactory());
		rabbitTemplate.setMessageConverter(convert());
//        rabbitTemplate.setMessagePropertiesConverter();
		rabbitTemplate.setEncoding("UTF-8");
		return rabbitTemplate;
	}
}