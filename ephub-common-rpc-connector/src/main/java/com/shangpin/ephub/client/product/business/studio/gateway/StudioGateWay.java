package com.shangpin.ephub.client.product.business.studio.gateway;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <p>Title:StudioSlotController.java </p>
 * <p>Description: studio批次rest服务接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author wangchao
 * @date 2017年06月21日 下午3:52:56
 */
@FeignClient("ephub-product-business-service")
public interface StudioGateWay {
	
	@RequestMapping(value = "/studio-slot/create", method = RequestMethod.POST,consumes = "application/json")
	public Boolean createStudioSlot();
	
	@RequestMapping(value = "/studio-slot/check", method = RequestMethod.POST,consumes = "application/json")
	public Boolean checkStudioSlot();
	
	@RequestMapping(value = "/studio-slot/downloadImage", method = RequestMethod.POST,consumes = "application/json")
	public Boolean downLoadImageByFtp();
	
}
