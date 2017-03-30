package com.shangpin.ephub.price.consumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
* <p>Description: 价格处理消费者</p>

 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.shangpin.ephub")
@EnableScheduling
public class PriceConsumerServiceApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(PriceConsumerServiceApplication.class, args);
	}
}
