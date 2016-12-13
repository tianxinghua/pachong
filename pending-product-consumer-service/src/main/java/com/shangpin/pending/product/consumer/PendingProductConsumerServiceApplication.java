package com.shangpin.pending.product.consumer;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.shangpin.pending.product.consumer.conf.clients.mysql.brand.HubBrandDicClient;
import com.shangpin.pending.product.consumer.conf.clients.mysql.brand.bean.HubBrandDic;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class PendingProductConsumerServiceApplication implements ApplicationRunner{

	public static void main(String[] args) {
		SpringApplication.run(PendingProductConsumerServiceApplication.class, args);
	}
	@Autowired
	private HubBrandDicClient client;
	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		HubBrandDic hubBrandDic = new HubBrandDic();
		hubBrandDic.setCreateTime(new Date());
		hubBrandDic.setCreateUser("yanxiaobin");
		int insert = client.insert(hubBrandDic);
		System.out.println(insert);
	}
}
