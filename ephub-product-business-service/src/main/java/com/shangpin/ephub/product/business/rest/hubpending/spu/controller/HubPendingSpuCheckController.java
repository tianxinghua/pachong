package com.shangpin.ephub.product.business.rest.hubpending.spu.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.rest.hubpending.spu.dto.HubPendingSpuDto;
import com.shangpin.ephub.product.business.rest.hubpending.spu.result.HubPendingSpuCheckResult;
import com.shangpin.ephub.product.business.rest.hubpending.spu.service.HubPendingSpuCheckService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:HubCheckRuleController.java </p>
 * <p>Description: HubPendingSpu入库前校验规则rest服务接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月22 下午3:52:56
 */
@RestController
@RequestMapping(value = "/hub-pending-spu")
@Slf4j
public class HubPendingSpuCheckController {
	
	@Autowired
	private HubPendingSpuCheckService hubCheckRuleService;
	
	@RequestMapping(value = "/check-spu")
	public HubPendingSpuCheckResult checkPendingSpu(@RequestBody HubPendingSpuDto dto){
		log.info("");
		HubPendingSpuCheckResult result = new HubPendingSpuCheckResult();
		String returnStr = hubCheckRuleService.checkHubPendingSpu(dto);
		if(StringUtils.isNotBlank(returnStr)){
			result.setResult(returnStr);
			result.setPassing(false);
		}else{
			result.setPassing(true);
		}
		return result;
	}
}
