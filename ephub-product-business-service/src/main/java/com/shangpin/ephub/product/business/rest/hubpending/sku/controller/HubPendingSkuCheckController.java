package com.shangpin.ephub.product.business.rest.hubpending.sku.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.rest.hubpending.sku.dto.HubPendingSkuDto;
import com.shangpin.ephub.product.business.rest.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.product.business.rest.hubpending.sku.service.HubPendingSkuCheckService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:HubCheckRuleController.java </p>
 * <p>Description: HubPendingSku入库前校验规则rest服务接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月22 下午3:52:56
 */
@RestController
@RequestMapping(value = "/pending-sku")
@Slf4j
public class HubPendingSkuCheckController {
	
	@Autowired
	private HubPendingSkuCheckService hubCheckRuleService;
	
	@RequestMapping(value = "/check-sku")
	public HubPendingSkuCheckResult checkSku(@RequestBody HubPendingSkuDto dto){
		log.info("");
		HubPendingSkuCheckResult result = new HubPendingSkuCheckResult();
		String returnStr = hubCheckRuleService.checkHubPendingSku(dto);
		if(StringUtils.isNotBlank(returnStr)){
			result.setResult(returnStr);
			result.setPassing(false);
		}else{
			result.setPassing(true);
		}
		return result;
	}
}
