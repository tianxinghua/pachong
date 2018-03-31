package com.shangpin.ephub.client.product.business.hubpending.spu.gateway;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.response.HubResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

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





}
