package com.shangpin.ephub.client.business.slotspupic.gateway;

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
public interface SlotSpuPicBusinessGateWay {
	

	@RequestMapping(value="/slot-spu-pic/judge-spu-exist" ,method=RequestMethod.POST ,consumes = "application/json")
	public boolean judgeSlotSpuExist(@RequestBody HubSlotSpuDto slotSpuDto);




}
