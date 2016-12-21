package com.shangpin.pending.product.consumer;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.gender.gateway.HubGenderDicGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.shangpin.ephub")
public class PendingProductConsumerServiceApplication implements ApplicationRunner{

	public static void main(String[] args) {

		SpringApplication.run(PendingProductConsumerServiceApplication.class, args);
	}
	@Autowired
	private HubSupplierSpuGateWay client;
	@Override
	public void run(ApplicationArguments args) throws Exception {
		HubSupplierSpuDto arg0 = new HubSupplierSpuDto();
		arg0.setSupplierId("S123456");
		arg0.setCreateTime(new Date());
		arg0.setSupplierBrandname("PDFAD");
		client.insert(arg0 );
		HubSupplierSpuDto dto = client.selectByPrimaryKey(1L);
		System.out.println(dto);
	}
}
