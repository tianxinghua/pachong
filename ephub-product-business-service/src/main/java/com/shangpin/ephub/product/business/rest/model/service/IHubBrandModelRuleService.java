package com.shangpin.ephub.product.business.rest.model.service;
/**
 * <p>Title:IHubBrandModelRuleService.java </p>
 * <p>Description: 品牌型号规则接口规范</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月21日 下午4:14:49
 */
public interface IHubBrandModelRuleService {
	/**
	 * 正则校验
	 * @param hubBrandNo 品牌编号
	 * @param hubCategoryNo 品类编号
	 * @param brandMode 品牌方型号
	 * @return 如果校验通过将返回通过的品牌型号，否则将返回null；
	 */
	String regexVerify(String hubBrandNo, String hubCategoryNo, String brandMode);
	/**
	 * 规则校验
	 * @param hubBrandNo 品牌编号
	 * @param hubCategoryNo 品类编号
	 * @param brandMode 品牌方型号
	 * @return 如果校验通过将返回通过的品牌型号，否则将返回null；
	 */
	String ruleVerify(String hubBrandNo, String hubCategoryNo, String brandMode);

}
