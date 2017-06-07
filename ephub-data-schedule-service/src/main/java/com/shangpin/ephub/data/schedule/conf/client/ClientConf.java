package com.shangpin.ephub.data.schedule.conf.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Retryer;

@Configuration
public class ClientConf {

	@Bean
	Retryer retryer(){
		return Retryer.NEVER_RETRY;
	}
	
}
