package com.shangpin.ephub.client.data.mysql.product.gateway;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.product.dto.SpuModelDto;

/**
 *
 * 待审核处理
 *
 */
@FeignClient("ephub-data-mysql-service")
public interface PengdingToHubGateWay {

	@RequestMapping(value = "/penging-to-hub/create-hubspu-and-hubsku", method = RequestMethod.POST,consumes = "application/json")
    public boolean auditPending(@RequestBody SpuModelDto dto);


	

}
