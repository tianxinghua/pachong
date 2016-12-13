package com.shangpin.picture.product.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PictureProductConsumerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PictureProductConsumerServiceApplication.class, args);
	}
}
