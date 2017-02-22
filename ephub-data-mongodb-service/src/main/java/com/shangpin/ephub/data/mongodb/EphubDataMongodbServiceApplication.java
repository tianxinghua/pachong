package com.shangpin.ephub.data.mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**
 * <p>Title:EphubDataMongodbServiceApplication.java </p>
 * <p>Description: 系统入口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年2月22日 上午11:09:12
 */
@SpringBootApplication(exclude = {RedisAutoConfiguration.class,RedisRepositoriesAutoConfiguration.class})
@EnableDiscoveryClient
public class EphubDataMongodbServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EphubDataMongodbServiceApplication.class, args);
	}
}
