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
	/**
	 * 正则校验
	 * @param hubBrandNo 品牌编号
	 * @param hubCategoryNo 品类编号
	 * @param brandMode 品牌方型号
	 * @return 如果校验通过将返回通过的品牌型号，否则将返回null；
	 */
	@Override
	public String regexVerify(String hubBrandNo, String hubCategoryNo, String brandMode) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 规则校验
	 * @param hubBrandNo 品牌编号
	 * @param hubCategoryNo 品类编号
	 * @param brandMode 品牌方型号
	 * @return 如果校验通过将返回通过的品牌型号，否则将返回null；
	 */
	@Override
	public String ruleVerify(String hubBrandNo, String hubCategoryNo, String brandMode) {
		// TODO Auto-generated method stub
		return null;
	}
}
