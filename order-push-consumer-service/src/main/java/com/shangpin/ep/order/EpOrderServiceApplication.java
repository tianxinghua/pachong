package com.shangpin.ep.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
/**
 * EP订单实时推送系统入口
 * <p>Title:EpOrderServiceApplication.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p>
 * @author yanxiaobin
 * @date 2016年11月9日 上午11:06:26
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableScheduling
@EnableFeignClients(value = "com.shangpin.ephub")
@ComponentScan(basePackages={"com.shangpin"})
public class EpOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EpOrderServiceApplication.class, args);
	}
}
