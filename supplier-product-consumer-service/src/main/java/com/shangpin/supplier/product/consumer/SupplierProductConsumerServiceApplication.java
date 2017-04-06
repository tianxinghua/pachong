package com.shangpin.supplier.product.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.shangpin.supplier.product.consumer.conf.client.ClientConf;
/**
 * <p>Title:SupplierProductConsumerServiceApplication.java </p>
 * <p>Description: 项目启动入口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月12日 下午7:23:28
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(value = "com.shangpin.ephub", defaultConfiguration = ClientConf.class)
@EnableScheduling
public class SupplierProductConsumerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupplierProductConsumerServiceApplication.class, args);
	}

}
