package com.shangpin.ephub.product.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.shangpin.ephub")
public class EphubProductBusinessServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EphubProductBusinessServiceApplication.class, args);
	}
}
