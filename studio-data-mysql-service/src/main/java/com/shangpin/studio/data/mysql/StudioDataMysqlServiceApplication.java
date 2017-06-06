package com.shangpin.studio.data.mysql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**
 * <p>Title:EphubDataMysqlServiceApplication.java </p>
 * <p>Description: 系统入口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年2月22日 上午11:08:30
 */
@SpringBootApplication(exclude = {RedisAutoConfiguration.class,RedisRepositoriesAutoConfiguration.class})
@EnableDiscoveryClient
public class StudioDataMysqlServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudioDataMysqlServiceApplication.class, args);
	}
}
