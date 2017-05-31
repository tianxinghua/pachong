package com.shangpin.ephub.client.consumer.hubskusuppliermapping.gateway;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.consumer.hubskusuppliermapping.dto.ProductMessageDto;

/**
    SKU选品服务
 */
@FeignClient("asynchronous-task-consumer-service")
public interface SkuSupplierMappingSelectGateWay {
	
	/**

	 */
	@RequestMapping(value = "/hub-sku-supplier-mapping/select", method = RequestMethod.POST, consumes = "application/json")
	public void select(@RequestBody ProductMessageDto productMessageDto);
}
