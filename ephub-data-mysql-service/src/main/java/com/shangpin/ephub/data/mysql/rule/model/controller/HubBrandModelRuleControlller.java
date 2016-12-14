package com.shangpin.ephub.data.mysql.rule.model.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.rule.model.service.HubBrandModelRuleService;

/**
 * <p>Title:HubBrandModelRuleControlller.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:56:41
 */
@RestController
@RequestMapping("/hub-brand-model-rule")
public class HubBrandModelRuleControlller {

	@Autowired
	private HubBrandModelRuleService hubBrandModelRuleService;
}
