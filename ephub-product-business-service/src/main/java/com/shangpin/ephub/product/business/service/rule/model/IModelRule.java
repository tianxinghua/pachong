package com.shangpin.ephub.product.business.service.rule.model;
/**
 * 型号规则
 * <p>Title:IModelRule.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月20日 下午5:27:18
 */
public interface IModelRule {
	/**
	 * 检查供应商品牌型号规则是否通过
	 * @param model 供应商品牌型号 
	 * @return 如果通过将返回true；否则返回false；
	 */
	public String checkModelRule(String model);
}
