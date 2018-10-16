package com.shangpin.ep.order.conf.client;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
