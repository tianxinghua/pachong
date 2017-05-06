package com.shangpin.ephub.client.product.business.hubpending.spu.gateway;

import com.shangpin.ephub.client.data.mysql.product.dto.SpuModelDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
   spu pending audit
 */
@FeignClient("asynchronous-task-consumer-service")
public interface HubSpuPendingAuditGateWay {
	
	/**
	 *  审核
	 */
	@RequestMapping(value = "/hub-spu-pending/audit", method = RequestMethod.POST,consumes = "application/json")
	public boolean auditSpu(@RequestBody SpuModelDto dto);
	

}
