package com.shangpin.ephub.client.business.stuidiotimelag.gateway;

import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
  slotspupic 处理
 *
 */
@FeignClient("ephub-product-business-service")
public interface StudioTimeLagBusinessGateWay {
	

	@RequestMapping(value="/studio-time/get-time-lag" ,method=RequestMethod.POST ,consumes = "application/json")
	public int  getTimeLag(@RequestBody Long studioId);


	@RequestMapping(value="/studio-time/get-time-lag-time" ,method=RequestMethod.POST ,consumes = "application/json")
	public String   getTimeLagTime(@RequestBody Long studioId);




}
