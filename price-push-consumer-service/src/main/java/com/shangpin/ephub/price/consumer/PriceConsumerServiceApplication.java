package com.shangpin.ephub.price.consumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
* <p>Description: 价格处理消费者</p>

 */
@SpringBootApplication(exclude = {RedisAutoConfiguration.class,RedisRepositoriesAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients("com.shangpin.ephub")
@ComponentScan(basePackages={"com.shangpin"})
@EnableScheduling
public class PriceConsumerServiceApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(PriceConsumerServiceApplication.class, args);
	}
}
