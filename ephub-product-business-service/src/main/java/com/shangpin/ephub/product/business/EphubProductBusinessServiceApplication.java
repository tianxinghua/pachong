package com.shangpin.ephub.product.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EphubProductBusinessServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EphubProductBusinessServiceApplication.class, args);
	}
}
