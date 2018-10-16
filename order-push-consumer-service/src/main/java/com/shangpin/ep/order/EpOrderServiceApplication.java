package com.shangpin.ep.order;

import com.shangpin.ep.order.conf.client.ClientConf;
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
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(value = "com.shangpin.ephub", defaultConfiguration = ClientConf.class)
@EnableScheduling
public class EpOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EpOrderServiceApplication.class, args);
	}
}
