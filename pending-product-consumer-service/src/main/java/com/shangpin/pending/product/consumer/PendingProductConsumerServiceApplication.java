package com.shangpin.pending.product.consumer;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubBrandDicGateway;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.categroy.gateway.HubSupplierCategroyDicGateWay;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicDto;
import com.shangpin.ephub.client.data.mysql.gender.gateway.HubGenderDicGateWay;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleCriteriaDto;
import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleCriteriaDto.Criteria;
import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleDto;
import com.shangpin.ephub.client.data.mysql.rule.gateway.HubBrandModelRuleGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.shangpin.ephub")
public class PendingProductConsumerServiceApplication implements ApplicationRunner {

	public static void main(String[] args) {

		SpringApplication.run(PendingProductConsumerServiceApplication.class, args);
	}

	@Autowired
	private HubSupplierValueMappingGateWay client;

	@Autowired
	private HubBrandDicGateway clients;
	@Override
	public void run(ApplicationArguments args) throws Exception {
		HubBrandDicCriteriaDto arg1 = new HubBrandDicCriteriaDto();
		
		List<HubBrandDicDto> list2 = clients.selectByCriteria(arg1);
		System.out.println(list2);
		HubSupplierValueMappingCriteriaDto arg0 = new HubSupplierValueMappingCriteriaDto();
		arg0.createCriteria().andColumnMappingIdEqualTo(1L);
		List<HubSupplierValueMappingDto> list = client.selectByCriteria(arg0);
		System.out.println(list);
	}
}
