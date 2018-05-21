package com.shangpin.ephub.client.product.business.hubpending.spu.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.response.HubResponse;

/**
 * <p>Title:HubBrandModelRuleController.java </p>
 * <p>Description: HubPendingSku数据入库前校验rest服务接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月23日 下午3:52:56
 */
@FeignClient("ephub-product-business-service")
public interface HubPendingHandleGateWay {

	/**
	 * HUB pending 图片处理
	 * @param spPicUrl 图片列表
	 * @return 校验结果：
	 */
	@RequestMapping(value = "/pending-product/retry-pictures", method = RequestMethod.POST,consumes = "application/json")
	public HubResponse<?> retryPictures(@RequestBody List<String> spPicUrl);

	@RequestMapping(value = "/pending-product-rest/spu-pending-audit/{spuPendingId}", method = RequestMethod.POST,consumes = "application/json")
	public String audit(@PathVariable("spuPendingId") Long spuPendingId);
}
