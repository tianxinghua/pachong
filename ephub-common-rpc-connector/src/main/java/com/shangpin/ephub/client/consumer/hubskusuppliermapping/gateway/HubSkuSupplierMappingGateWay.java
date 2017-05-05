package com.shangpin.ephub.client.consumer.hubskusuppliermapping.gateway;

import com.shangpin.ephub.client.consumer.hubskusuppliermapping.dto.ProductMessageDto;
import com.shangpin.ephub.client.consumer.picture.dto.RetryPictureDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
    SKU选品服务
 */
@FeignClient("asynchronous-task-consumer-service")
public interface HubSkuSupplierMappingGateWay {
	
	/**

	 */
	@RequestMapping(value = "/hub-sku-supplier-mapping/select", method = RequestMethod.POST, consumes = "application/json")
	public void select(@RequestBody ProductMessageDto productMessageDto);
}
