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
		if (StringUtils.isBlank(hubBrandNo)) {
			log.warn("系统检测到接收到的品牌编号为空，品牌方型号校验不通过");
			return null;
		}
		if (StringUtils.isBlank(brandMode)) {
			log.warn("系统检测到加工之前的品牌方型号为空，品牌方型号校验不通过");
			return null;
		}
		return verifyUseBrandNo(hubBrandNo, brandMode);
	}
	/**
	 * 使用品牌编号校验品牌型号
	 * @param hubBrandNo 品牌编号
	 * @param brandMode 品牌型号
	 * @return 如果校验通过将返回通过的品牌型号，否则将返回null；
	 */
	private String verifyUseBrandNo(String hubBrandNo, String brandMode) {
		HubBrandModelRuleCriteriaDto criteria = new HubBrandModelRuleCriteriaDto();
		criteria.createCriteria().andHubBrandNoEqualTo(hubBrandNo);
		List<HubBrandModelRuleDto> hubBrandModelRuleDtoList = brandModelRuleManager.findByCriteria(criteria);
		if (CollectionUtils.isEmpty(hubBrandModelRuleDtoList)) {
			return brandMode;
		} else {
			String result = null;
			for (HubBrandModelRuleDto hubBrandModelRuleDto : hubBrandModelRuleDtoList) {
				String modelRex = hubBrandModelRuleDto.getModelRex();
				String excludeRex = hubBrandModelRuleDto.getExcludeRex();
				String separator = hubBrandModelRuleDto.getFormatSeparator();
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
	 * 规则校验：按照领导的要求，业务需求暂时不需要这种校验方式，因此暂不实现，等将来业务需求需要时再实现！！！！！！
	 * @param hubBrandNo 品牌编号
	 * @param hubCategoryNo 品类编号
	 * @param brandMode 品牌方型号
	 * @return 如果校验通过将返回通过的品牌型号，否则将返回null；
	 */
	@Override
	public String ruleVerify(String hubBrandNo, String hubCategoryNo, String brandMode) {
		// TODO Auto-generated method stub (B[0-9]{2})(C[0-9]{2})(D[0-9]{2})
		return null;
	}
	/**
	 * 既校验品牌又校验品类的正则校验
	 * @param hubBrandNo 品牌编号
	 * @param hubCategoryNo 品类编号
	 * @param brandMode 品牌方型号
	 * @return 如果校验通过将返回通过的品牌型号，否则将返回null；
	 */
	@Override
	public String regexVerifyWithCategory(String hubBrandNo, String hubCategoryNo, String brandMode) {
		if (StringUtils.isBlank(hubBrandNo)) {
			log.warn("系统检测到接收到的品牌编号为空，品牌方型号校验不通过");
			return null;
		}
		if (StringUtils.isBlank(brandMode)) {
			log.warn("系统检测到加工之前的品牌方型号为空，品牌方型号校验不通过");
			return null;
		}
		if (StringUtils.isBlank(hubCategoryNo)) {//如果品类编号为空
			return verifyUseBrandNo(hubBrandNo, brandMode);
		} else {//如果品类编号不为空
			return recursiveVerifyWithCategory(hubBrandNo, hubCategoryNo, brandMode);
		}
	}
	/**
	 * 根据品类递归结合品牌校验货号规则
	 * @param hubBrandNo 品牌编号
	 * @param hubCategory 品类对象
	 * @param brandMode 货号
	 * @return 校验结果
	 */
	private String recursiveVerifyWithCategory(String hubBrandNo, String hubCategory, String brandMode) {
		String result= null;
		int length = hubCategory.length();
		switch (length) {
		case 3://一级
			List<HubBrandModelRuleDto> hubBrandModelRuleDtoList = getRegxRuleListByHubBrandNoAndHubCategory(hubBrandNo, hubCategory);
			if (CollectionUtils.isEmpty(hubBrandModelRuleDtoList)) {
				result = verifyUseBrandNo(hubBrandNo, brandMode);
			} else {
				result = verify(hubBrandModelRuleDtoList, brandMode);
			}			
			break;
		case 6://二级
			List<HubBrandModelRuleDto> ___hubBrandModelRuleDtoList = getRegxRuleListByHubBrandNoAndHubCategory(hubBrandNo, hubCategory);
			if (CollectionUtils.isEmpty(___hubBrandModelRuleDtoList)) {
				result = recursiveVerifyWithCategory(hubBrandNo, hubCategory.substring(0, 3), brandMode);
			} else {
				result = verify(___hubBrandModelRuleDtoList, brandMode);
			}			
			break;
		case 9://三级
			List<HubBrandModelRuleDto> __hubBrandModelRuleDtoList = getRegxRuleListByHubBrandNoAndHubCategory(hubBrandNo, hubCategory);
			if (CollectionUtils.isEmpty(__hubBrandModelRuleDtoList)) {
				result = recursiveVerifyWithCategory(hubBrandNo, hubCategory.substring(0, 6), brandMode);
			} else {
				result = verify(__hubBrandModelRuleDtoList, brandMode);
			}		
			break;	
		case 12://四级
			List<HubBrandModelRuleDto> _hubBrandModelRuleDtoList = getRegxRuleListByHubBrandNoAndHubCategory(hubBrandNo, hubCategory);
			if (CollectionUtils.isEmpty(_hubBrandModelRuleDtoList)) {
				result = recursiveVerifyWithCategory(hubBrandNo, hubCategory.substring(0, 9), brandMode);
			} else {
				result = verify(_hubBrandModelRuleDtoList, brandMode);
			}
			break;			
		default://未知
			result = null;//未知品类级别则校验不通过
			break;
		}
		return result;
	}
	/**
	 * 根据品牌编号和品类进行校验货号是否符合规则
	 * @param hubBrandNo 品牌编号
	 * @param hubCategory 品类
	 * @param brandMode 货号
	 * @return 通过则非空字符串货号返回，否则返回null
	 */
	private String verify(List<HubBrandModelRuleDto> hubBrandModelRuleDtoList, String brandMode) {
			String result = null;
			for (HubBrandModelRuleDto hubBrandModelRuleDto : hubBrandModelRuleDtoList) {
				String modelRex = hubBrandModelRuleDto.getModelRex();
				String excludeRex = hubBrandModelRuleDto.getExcludeRex();
				String separator = hubBrandModelRuleDto.getFormatSeparator();
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
	
	/**
	 * 获取规则
	 * @param hubBrandNo
	 * @param hubCategory
	 * @return
	 */
	private List<HubBrandModelRuleDto> getRegxRuleListByHubBrandNoAndHubCategory(String hubBrandNo,
			String hubCategory) {
		HubBrandModelRuleCriteriaDto criteria = new HubBrandModelRuleCriteriaDto();
		criteria.createCriteria().andHubBrandNoEqualTo(hubBrandNo).andHubCategoryNoEqualTo(hubCategory);
		List<HubBrandModelRuleDto> hubBrandModelRuleDtoList = brandModelRuleManager.findByCriteria(criteria);
		return hubBrandModelRuleDtoList;
	}
}
