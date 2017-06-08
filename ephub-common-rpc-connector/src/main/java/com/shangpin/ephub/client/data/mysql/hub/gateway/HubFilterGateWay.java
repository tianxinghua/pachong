package com.shangpin.ephub.client.data.mysql.hub.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.hub.dto.HubFilterRequest;
import com.shangpin.ephub.client.data.mysql.hub.dto.HubFilterResponse;

/**
 * <p>HubWaitSelectGateWay.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月28日 下午16:52:02
 */
@FeignClient("ephub-data-mysql-service")
public interface HubFilterGateWay {

	@RequestMapping(value = "/hub-filter/select", method = RequestMethod.POST,consumes = "application/json")
    public List<HubFilterResponse> select(@RequestBody HubFilterRequest request);
	
}
