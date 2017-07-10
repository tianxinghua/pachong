package com.shangpin.ephub.client.product.business.studio.gateway;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
/**
 * <p>Title: HubSlotSpuTaskGateWay</p>
 * <p>Description: 待拍照导入导出等一些任务接口 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月3日 上午11:28:32
 *
 */
@FeignClient("ephub-product-business-service")
public interface HubSlotSpuTaskGateWay {
	
	@RequestMapping(value="/hub-slot-spu/add-slot-spu" ,method=RequestMethod.POST ,consumes = "application/json")
	public boolean add(@RequestBody HubSpuPendingDto hubSpuPendingDto);

}
