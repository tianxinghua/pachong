package com.shangpin.ephub.client.business.supplier.gateway;

import com.shangpin.ephub.client.business.supplier.dto.SupplierInHubDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
   获取供货商信息
 */
@FeignClient("ephub-product-business-service")
public interface SupplierInHubGateWay {
	
	@RequestMapping(value = "/supplier-in-hub/is-need-send", method = RequestMethod.POST,consumes = "application/json")
	public Boolean isNeedSend(@RequestBody String supplierId);
	
	@RequestMapping(value = "/supplier-in-hub/get-supplier", method = RequestMethod.POST,consumes = "application/json")
	public SupplierInHubDto getSupplierMsg(@RequestBody String supplierId);
	

	
}
