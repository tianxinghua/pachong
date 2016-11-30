package com.shangpin.message.conf.mq;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:RabbitTemplateConf.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月29日 上午10:48:27
 */
@Configuration
@Slf4j
public class MQConf implements ApplicationRunner{
	
	@Autowired
	private AmqpAdmin amqpAdmin;
	/**
	 * 创建消息转换器，以便以JSON格式将消息发出
	 * @return JsonMessageConverter
	 */
	@Bean
	public Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
		return new Jackson2JsonMessageConverter();
	}
	/**
	 * <p>Title:MQConf.java </p>
	 * <p>Description: 库存队列配置</p>
	 * <p>Company: www.shangpin.com</p> 
	 * @author yanxiaobin
	 * @date 2016年11月29日 上午11:04:21
	 */
	public enum Module {
		/**
		 * 库存
		 */
		STOCK("HA.SupplierStockSync","HA.SupplierStockSyncQueue","#");
		/**
		 * 交换器
		 */
		private String exchange;
		/**
		 * 队列
		 */
		private String queue;
		/**
		 * 路由键
		 */
		private String key;
		/**
		 * 构造器
		 * @param exchange
		 * @param queue
		 * @param key
		 */
		private Module(String exchange, String queue, String key) {
			this.exchange = exchange;
			this.queue = queue;
			this.key = key;
		}
		public String getExchange() {
			return exchange;
		}
		public void setExchange(String exchange) {
			this.exchange = exchange;
		}
		public String getQueue() {
			return queue;
		}
		public void setQueue(String queue) {
			this.queue = queue;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
	}
	/**
	 * 系统启动之后立刻初始化队列配置
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("系统初始化Stock队列配置");
		DirectExchange directExchange = new DirectExchange(Module.STOCK.getExchange(), true, false);
		amqpAdmin.declareExchange(directExchange);
		Map<String, Object> settings = new HashMap<String, Object>();
		settings.put("x-max-priority", 2);
		Queue queue = new Queue(Module.STOCK.getQueue(), true, false, false, settings);
		amqpAdmin.declareQueue(queue);
		amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(directExchange).with(Module.STOCK.getKey()));
		log.info("系统初始化Stock队列配置完毕");
	}
}
