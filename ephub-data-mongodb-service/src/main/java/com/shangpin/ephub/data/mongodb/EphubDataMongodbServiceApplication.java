package com.shangpin.ephub.data.mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EphubDataMongodbServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EphubDataMongodbServiceApplication.class, args);
	}
}
