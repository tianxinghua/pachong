package com.shangpin.supplier.product.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
/**
 * <p>Title:SupplierProductConsumerServiceApplication.java </p>
 * <p>Description: 项目启动入口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月12日 下午7:23:28
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.shangpin.ephub")
public class SupplierProductConsumerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupplierProductConsumerServiceApplication.class, args);
	}
}
