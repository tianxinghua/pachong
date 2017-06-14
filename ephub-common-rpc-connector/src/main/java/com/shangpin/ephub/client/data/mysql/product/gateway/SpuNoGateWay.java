package com.shangpin.ephub.client.data.mysql.product.gateway;

import com.shangpin.ephub.client.data.mysql.product.dto.HubPendingDto;
import com.shangpin.ephub.client.data.mysql.product.dto.SpuModelDto;
import com.shangpin.ephub.client.data.mysql.product.dto.SpuNoTypeDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * pendingToHub处理
 *
 */
@FeignClient("ephub-data-mysql-service")
public interface SpuNoGateWay {

	@RequestMapping(value = "/spuno/getspuno", method = RequestMethod.POST,consumes = "application/json")
    public String getSpuNo(@RequestBody SpuNoTypeDto dto);


	

}
