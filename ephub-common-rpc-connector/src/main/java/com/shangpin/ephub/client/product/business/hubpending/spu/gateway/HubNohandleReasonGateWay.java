package com.shangpin.ephub.client.product.business.hubpending.spu.gateway;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.product.business.hubpending.spu.dto.NohandleReason;
/**
 * <p>Title: HubNohandleReasonGateWay</p>
 * <p>Description: 无法处理原因 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年8月7日 下午3:29:34
 *
 */
@FeignClient("ephub-product-business-service")
public interface HubNohandleReasonGateWay {

	@RequestMapping(value = "/pending-nohandle-reason/insert", method = RequestMethod.POST,consumes = "application/json")
	public boolean insertNohandleReason(NohandleReason nohandleReason);
}
