package com.shangpin.ephub.product.business.rest.hubpending.sku.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
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
	public HubPendingSkuCheckResult checkSku(@RequestBody HubSkuPendingDto dto){
		log.info("校验pendingSku接受到参数:{}",dto);
		
		HubPendingSkuCheckResult result = hubCheckRuleService.checkHubPendingSku(dto);
		return result;
	}


}
