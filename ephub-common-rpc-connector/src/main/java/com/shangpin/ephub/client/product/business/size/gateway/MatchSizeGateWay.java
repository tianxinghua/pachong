package com.shangpin.ephub.client.product.business.size.gateway;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.product.business.size.dto.MatchSizeDto;
import com.shangpin.ephub.client.product.business.size.result.MatchSizeResult;

/**
 * <p>Title:HubBrandModelRuleController.java </p>
 * <p>Description: HUB品牌型号规则rest服务接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月21日 下午3:52:56
 */
@FeignClient("ephub-product-business-service")
public interface MatchSizeGateWay {
	
	/**
	 * 校验供应商品牌型号是否符合品牌方型号规则
	 * @param dto 数据传输对象
	 * @return 校验结果：包含是否校验通过以及校验之后的结果（校验通过的经过加工的品牌型号）
	 */
	@RequestMapping(value = "/match-size/match", method = RequestMethod.POST,consumes = "application/json")
	public MatchSizeResult matchSize(MatchSizeDto dto);
}
