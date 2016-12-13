package com.shangpin.ephub.data.mysql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EphubDataMysqlServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EphubDataMysqlServiceApplication.class, args);
	}
}
