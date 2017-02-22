package com.shangpin.supplier.product.consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.shangpin.ephub.client.data.mongodb.product.supplier.gateway.SupplierProductGateway;
import com.shangpin.supplier.product.consumer.supplier.deliberti.dto.DelibertiSpuDto;
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
@EnableScheduling
public class SupplierProductConsumerServiceApplication implements ApplicationRunner{

	public static void main(String[] args) {
		SpringApplication.run(SupplierProductConsumerServiceApplication.class, args);
	}

	@Autowired
	private SupplierProductGateway gateway;
	@Override
	public void run(ApplicationArguments args) throws Exception {
		Map<String, Object> data = new HashMap<>();
		DelibertiSpuDto spu = new DelibertiSpuDto();
		spu.setBrandId("brandId");
		spu.setCategoryId("categoryId");
		data.put("spu", spu);
		List<String> skus = new ArrayList<>();
		skus.add("AAAAA");
		skus.add("BBBBB");
		skus.add("CCCCC");
		data.put("skus", skus);
		boolean flag = gateway.save(data);
		System.out.println(flag);
	}
}
