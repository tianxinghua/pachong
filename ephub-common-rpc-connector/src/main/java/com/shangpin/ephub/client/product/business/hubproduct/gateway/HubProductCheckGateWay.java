package com.shangpin.ephub.client.product.business.hubproduct.gateway;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.product.business.hubproduct.dto.HubProductDto;
import com.shangpin.ephub.client.product.business.hubproduct.result.HubProductCheckResult;

/**
 * <p>Title:HubBrandModelRuleController.java </p>
 * <p>Description: HUB数据入库前校验rest服务接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月23日 下午3:52:56
 */
@FeignClient("ephub-product-business-service")
public interface HubProductCheckGateWay {
	
	/**
	 * HUB数据入库前校验
	 * @param dto 数据传输对象
	 * @return 校验结果：
	 */
	@RequestMapping(value = "/hub-product-check/check-product", method = RequestMethod.POST,consumes = "application/json")
	public HubProductCheckResult checkProduct(@RequestBody HubProductDto dto);
}
