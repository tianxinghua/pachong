package com.shangpin.pending.product.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.shangpin.pending.product.consumer.conf.client.ClientConf;

@SpringBootApplication(exclude = {RedisAutoConfiguration.class,RedisRepositoriesAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients(value = "com.shangpin.ephub", defaultConfiguration = ClientConf.class)
@EnableScheduling
public class PendingProductConsumerServiceApplication  {

    public static void main(String[] args) {

        SpringApplication.run(PendingProductConsumerServiceApplication.class, args);
    }
}
