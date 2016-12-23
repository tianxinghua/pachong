package com.shangpin.supplier.product.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
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
public class SupplierProductConsumerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupplierProductConsumerServiceApplication.class, args);
	}
//	@Autowired
//	private HubSupplierSpuGateWay client;
//	@Override
//	public void run(ApplicationArguments args) throws Exception {
//		HubSupplierSpuDto arg0 = new HubSupplierSpuDto();
//		arg0.setSupplierId("S666666");
//		arg0.setCreateTime(new Date());
//		arg0.setSupplierBrandname("YANXIAOBIN");
//		client.insert(arg0 );
////		HubSupplierSpuDto dto = client.selectByPrimaryKey(1L);
////		System.out.println(dto);
//		
//	}
}
