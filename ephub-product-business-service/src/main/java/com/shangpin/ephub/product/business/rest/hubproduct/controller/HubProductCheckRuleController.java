package com.shangpin.ephub.product.business.rest.hubproduct.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.rest.hubproduct.dto.HubProductDto;
import com.shangpin.ephub.product.business.rest.hubproduct.result.HubProductCheckRuleResult;
import com.shangpin.ephub.product.business.rest.hubproduct.service.HubCheckRuleService;
import com.shangpin.ephub.product.business.rest.model.dto.BrandModelDto;
import com.shangpin.ephub.product.business.rest.model.result.BrandModelResult;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:HubCheckRuleController.java </p>
 * <p>Description: HUB入库前校验规则rest服务接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月22 下午3:52:56
 */
@RestController
@RequestMapping(value = "/hub-check")
@Slf4j
public class HubProductCheckRuleController {
	
	@Autowired
	HubCheckRuleService hubCheckRuleService;
	
	@RequestMapping(value = "/product")
	public HubProductCheckRuleResult checkProduct(@RequestBody HubProductDto dto){
		HubProductCheckRuleResult result = new HubProductCheckRuleResult();
		String returnStr = hubCheckRuleService.checkHubProduct(dto);
		if(StringUtils.isNotBlank(returnStr)){
			result.setResult(returnStr);
			result.setPassing(false);
		}else{
			result.setPassing(true);
		}
		return result;
	}
}
