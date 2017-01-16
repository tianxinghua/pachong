package com.shangpin.picture.product.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
/**
 * <p>Title:PictureProductConsumerServiceApplication.java </p>
 * <p>Description: 图片处理消费者</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月1日 下午7:32:45
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.shangpin.ephub")
public class PictureProductConsumerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PictureProductConsumerServiceApplication.class, args);
	}
}
