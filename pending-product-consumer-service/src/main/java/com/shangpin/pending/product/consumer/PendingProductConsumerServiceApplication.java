package com.shangpin.pending.product.consumer;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubBrandDicGateway;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.shangpin.ephub")
//
public class PendingProductConsumerServiceApplication implements ApplicationRunner{

	public static void main(String[] args) {

		SpringApplication.run(PendingProductConsumerServiceApplication.class, args);
	}
	@Autowired
	private HubBrandDicGateway client;
	@Override
	public void run(ApplicationArguments args) throws Exception {

		HubBrandDicDto hubBrandDic = new HubBrandDicDto();
		hubBrandDic.setCreateTime(new Date());
		hubBrandDic.setCreateUser("yanxiaobin");
		int i = client.insert(hubBrandDic);
		System.out.println(i);
	}
}
