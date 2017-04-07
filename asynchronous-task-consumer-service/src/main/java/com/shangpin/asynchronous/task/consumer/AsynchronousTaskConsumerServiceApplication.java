package com.shangpin.asynchronous.task.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.shangpin.asynchronous.task.consumer.conf.client.ClientConf;
/**
 * <p>Title:AsynchronousTaskConsumerServiceApplication.java </p>
 * <p>Description: EPHUB系统后台异步任务消费系统入口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月19日 下午7:08:33
 */
@SpringBootApplication(exclude = {RedisAutoConfiguration.class,RedisRepositoriesAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients(value = "com.shangpin.ephub", defaultConfiguration = ClientConf.class)
public class AsynchronousTaskConsumerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsynchronousTaskConsumerServiceApplication.class, args);
	}
}
