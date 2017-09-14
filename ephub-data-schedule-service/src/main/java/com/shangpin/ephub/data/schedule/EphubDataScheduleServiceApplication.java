package com.shangpin.ephub.data.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.shangpin.ephub.data.schedule.conf.client.ClientConf;

/**
 * <p>Title:EphubDataScheduleServiceApplication.java </p>
 * <p>Description: 系统入口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年2月22日 上午11:08:30
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableFeignClients(value = "com.shangpin.ephub", defaultConfiguration = ClientConf.class)
public class EphubDataScheduleServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EphubDataScheduleServiceApplication.class, args);
	}
}
