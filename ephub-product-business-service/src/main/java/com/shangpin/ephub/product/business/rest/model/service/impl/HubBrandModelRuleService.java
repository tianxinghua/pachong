package com.shangpin.ephub.product.business.rest.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.product.business.rest.model.manager.HubBrandModelRuleManager;
import com.shangpin.ephub.product.business.rest.model.service.IHubBrandModelRuleService;

/**
 * <p>Title:HubBrandModelRuleService.java </p>
 * <p>Description: 品牌型号规则接口规范实现</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月21日 下午4:15:16
 */
@Service
public class HubBrandModelRuleService implements IHubBrandModelRuleService {

	@Autowired
	private HubBrandModelRuleManager brandModelRuleManager;
}
