package com.shangpin.ephub.data.mysql.rule.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.rule.model.bean.HubBrandModelRuleCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.rule.model.bean.HubBrandModelRuleWithCriteria;
import com.shangpin.ephub.data.mysql.rule.model.mapper.HubBrandModelRuleMapper;
import com.shangpin.ephub.data.mysql.rule.model.po.HubBrandModelRule;
import com.shangpin.ephub.data.mysql.rule.model.po.HubBrandModelRuleCriteria;

/**
 * <p>Title:HubBrandModelRuleService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:57:14
 */
@Service
public class HubBrandModelRuleService {

	@Autowired
	private HubBrandModelRuleMapper hubBrandModelRuleMapper;

	public int countByCriteria(HubBrandModelRuleCriteria criteria) {
		return hubBrandModelRuleMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubBrandModelRuleCriteria criteria) {
		return hubBrandModelRuleMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long brandModelRuleId) {
		return hubBrandModelRuleMapper.deleteByPrimaryKey(brandModelRuleId);
	}

	public int insert(HubBrandModelRule hubBrandModelRule) {
		return hubBrandModelRuleMapper.insert(hubBrandModelRule);
	}

	public int insertSelective(HubBrandModelRule hubBrandModelRule) {
		return hubBrandModelRuleMapper.insertSelective(hubBrandModelRule);
	}

	public List<HubBrandModelRule> selectByCriteriaWithRowbounds(
			HubBrandModelRuleCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubBrandModelRuleMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubBrandModelRule> selectByCriteria(HubBrandModelRuleCriteria criteria) {
		return hubBrandModelRuleMapper.selectByExample(criteria);
	}

	public HubBrandModelRule selectByPrimaryKey(Long brandModelRuleId) {
		return hubBrandModelRuleMapper.selectByPrimaryKey(brandModelRuleId);
	}

	public int updateByCriteriaSelective(HubBrandModelRuleWithCriteria hubBrandModelRuleWithCriteria) {
		return hubBrandModelRuleMapper.updateByExampleSelective(hubBrandModelRuleWithCriteria.getHubBrandModelRule(), hubBrandModelRuleWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubBrandModelRuleWithCriteria hubBrandModelRuleWithCriteria) {
		return hubBrandModelRuleMapper.updateByExample(hubBrandModelRuleWithCriteria.getHubBrandModelRule(), hubBrandModelRuleWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubBrandModelRule hubBrandModelRule) {
		return hubBrandModelRuleMapper.updateByPrimaryKeySelective(hubBrandModelRule);
	}

	public int updateByPrimaryKey(HubBrandModelRule hubBrandModelRule) {
		return hubBrandModelRuleMapper.updateByPrimaryKey(hubBrandModelRule);
	}
}
