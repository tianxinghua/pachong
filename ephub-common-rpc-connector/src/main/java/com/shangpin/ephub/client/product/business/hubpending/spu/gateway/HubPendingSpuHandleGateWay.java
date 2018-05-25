package com.shangpin.ephub.client.product.business.hubpending.spu.gateway;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;

/**
 * <p>Title:HubBrandModelRuleController.java </p>
 * <p>Description: HubPendingSku数据入库前校验rest服务接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月23日 下午3:52:56
 */
@FeignClient("ephub-product-business-service")
public interface HubPendingSpuHandleGateWay {
	
	/**
	 * HUB数据入库前校验
	 * @param dto 数据传输对象
	 * @return 校验结果：
	 */
	@RequestMapping(value = "/pending-spu-handle/pending-spu", method = RequestMethod.POST,consumes = "application/json")
	public HubSpuPendingDto handleHubPendingSpu(@RequestBody HubSpuPendingDto dto);
	
	/**
	 * HUB数据入库前校验
	 * @param dto 数据传输对象
	 * @return 校验结果：
	 */
	@RequestMapping(value = "/pending-spu-handle/update-spu/{spuPendingId}", method = RequestMethod.POST,consumes = "application/json")
	public Long updateSpuState(@PathVariable(value = "spuPendingId") Long spuPendingId);



}