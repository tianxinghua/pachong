package com.shangpin.asynchronous.task.consumer.conf.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Retryer;

/**
 * 客户端请求配置
 * @author yanxiaobin
 *
 */
@Configuration
public class ClientConf {

	@Bean
	Retryer retryer(){
		return Retryer.NEVER_RETRY;
	}
	
}
