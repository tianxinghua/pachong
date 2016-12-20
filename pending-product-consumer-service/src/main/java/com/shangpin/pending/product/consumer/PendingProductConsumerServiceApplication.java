package com.shangpin.pending.product.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.gender.gateway.HubGenderDicGateWay;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.shangpin.ephub")
public class PendingProductConsumerServiceApplication implements ApplicationRunner{

	public static void main(String[] args) {

		SpringApplication.run(PendingProductConsumerServiceApplication.class, args);
	}
	@Autowired
	private HubGenderDicGateWay client;
	@Override
	public void run(ApplicationArguments args) throws Exception {

//		HubGenderDicCriteriaDto arg0 = new HubGenderDicCriteriaDto();
//		arg0.createCriteria().andSupplierIdEqualTo("123424");
//		client.selectByCriteria(arg0 );
	}
}
