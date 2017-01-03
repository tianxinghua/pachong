package com.shangpin.ephub.fdfs.client.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**
 * <p>Title:EphubFdfsClientServiceApplication.java </p>
 * <p>Description: ephub图片服务器服务</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月2日 下午5:19:15
 */
@SpringBootApplication
@EnableDiscoveryClient
public class EphubFdfsClientServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EphubFdfsClientServiceApplication.class, args);
	}
}
