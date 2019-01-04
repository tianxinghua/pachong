package com.shangpin.api.airshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/** 
 * Date:     2016年1月12日 <br/> 
 * @author   陈小峰
 * @since    JDK 7
 */
@Configuration  
@EnableRedisHttpSession 
public class HttpSessionConfig {

	@Bean
	public static ConfigureRedisAction configureRedisAction() {
	    return ConfigureRedisAction.NO_OP;
	}
	
	
}
