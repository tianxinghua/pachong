package com.shangpin.ephub.client.consumer.pending.gateway;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.product.dto.SpuModelDto;

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
