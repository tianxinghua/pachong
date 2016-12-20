package com.shangpin.ephub.product.business.service.rule.model.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.product.business.service.rule.model.IModelRule;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title:ModelRule.java </p>
 * <p>Description: 型号规则实现</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月20日 下午5:28:31
 */
@Service
@Slf4j
public class PradaModelRule implements IModelRule {
	
	private static final String MACHINING_REGEX = "[^0-9a-zA-Z]";
	
	private static final String CHECK_REGEX = "([0-9a-zA-Z]{6})([0-9a-zA-Z]{3,4})([0-9a-zA-Z]{5})";
	/**
	 * 校验货号规则，如果校验通过，则返回货号，如果校验不通过，将返回null; 
	 */
	@Override
	public String checkModelRule(String model) {
		if (StringUtils.isBlank(model)) {
			log.warn("系统检测到加工之前的品牌方型号为空，品牌方型号校验不通过");
			return null; 
		}
		if (StringUtils.isBlank(model.replaceAll(MACHINING_REGEX, ""))) {
			log.warn("系统检测到加工之后的品牌方型号为空，品牌方型号校验不通过");
			return null;
		} else {
			if (model.matches(CHECK_REGEX)) {
				return model.replaceAll(CHECK_REGEX, "$1 $2 $3");
			} else {
				return null;
			}
		}
	}
}
