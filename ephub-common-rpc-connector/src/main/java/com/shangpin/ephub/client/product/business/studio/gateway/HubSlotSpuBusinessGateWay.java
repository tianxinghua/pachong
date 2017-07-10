package com.shangpin.ephub.client.product.business.studio.gateway;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <p>Title: HubSlotSpuTaskGateWay</p>
 * <p>Description: 待拍照导入导出等一些任务接口 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月3日 上午11:28:32
 *
 */
@FeignClient("ephub-product-business-service")
public interface HubSlotSpuBusinessGateWay {
	

	@RequestMapping(value="/hub-slot-spu/judge-spu-exist" ,method=RequestMethod.POST ,consumes = "application/json")
	public boolean judgeSlotSpuExist(@RequestBody HubSlotSpuDto slotSpuDto);




}
