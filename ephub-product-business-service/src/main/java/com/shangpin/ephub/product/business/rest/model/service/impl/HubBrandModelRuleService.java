package com.shangpin.ephub.product.business.rest.model.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleCriteriaDto;
import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleDto;
import com.shangpin.ephub.product.business.rest.model.manager.HubBrandModelRuleManager;
import com.shangpin.ephub.product.business.rest.model.service.IHubBrandModelRuleService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:HubBrandModelRuleService.java </p>
 * <p>Description: 品牌型号规则接口规范实现</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月21日 下午4:15:16
 */
@Service
@Slf4j
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
		if (StringUtils.isBlank(brandMode)) {
			log.warn("系统检测到加工之前的品牌方型号为空，品牌方型号校验不通过");
			return null;
		}
		HubBrandModelRuleCriteriaDto criteria = new HubBrandModelRuleCriteriaDto();
		criteria.createCriteria().andHubBrandNoEqualTo(hubBrandNo);
		List<HubBrandModelRuleDto> hubBrandModelRuleDtoList = brandModelRuleManager.findByCriteria(criteria);
		if (CollectionUtils.isEmpty(hubBrandModelRuleDtoList)) {
			return null;
		} else {
			String result = null;
			for (HubBrandModelRuleDto hubBrandModelRuleDto : hubBrandModelRuleDtoList) {
				String modelRex = hubBrandModelRuleDto.getModelRex();
				String excludeRex = hubBrandModelRuleDto.getExcludeRex();
				String separator = hubBrandModelRuleDto.getSeparator();
				if (StringUtils.isBlank(modelRex) || StringUtils.isBlank(excludeRex) || StringUtils.isBlank(separator)) {
					continue;
				}
				String processed = brandMode.replaceAll(excludeRex, "");
				if (StringUtils.isBlank(processed)) {
					log.warn("系统检测到加工之后的品牌方型号为空，品牌方型号校验不通过");
					continue;
				} else {
					if (processed.matches(modelRex)) {
						result = processed.replaceAll(modelRex, separator);
					} else {
						continue;
					}
				}
			}
			return result;
		}
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
