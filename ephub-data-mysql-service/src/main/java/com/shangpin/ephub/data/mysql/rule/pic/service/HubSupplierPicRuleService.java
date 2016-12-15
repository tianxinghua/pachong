package com.shangpin.ephub.data.mysql.rule.pic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.rule.pic.bean.HubSupplierPicRuleCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.rule.pic.bean.HubSupplierPicRuleWithCriteria;
import com.shangpin.ephub.data.mysql.rule.pic.mapper.HubSupplierPicRuleMapper;
import com.shangpin.ephub.data.mysql.rule.pic.po.HubSupplierPicRule;
import com.shangpin.ephub.data.mysql.rule.pic.po.HubSupplierPicRuleCriteria;

/**
 * <p>Title:HubSupplierPicRuleService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:59:02
 */
@Service
public class HubSupplierPicRuleService {

	@Autowired
	private HubSupplierPicRuleMapper hubSupplierPicRuleMapper;

	public int countByCriteria(HubSupplierPicRuleCriteria criteria) {
		return hubSupplierPicRuleMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSupplierPicRuleCriteria criteria) {
		return hubSupplierPicRuleMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long supplierPicRuleId) {
		return hubSupplierPicRuleMapper.deleteByPrimaryKey(supplierPicRuleId);
	}

	public int insert(HubSupplierPicRule hubSupplierPicRule) {
		return hubSupplierPicRuleMapper.insert(hubSupplierPicRule);
	}

	public int insertSelective(HubSupplierPicRule hubSupplierPicRule) {
		return hubSupplierPicRuleMapper.insertSelective(hubSupplierPicRule);
	}

	public List<HubSupplierPicRule> selectByCriteriaWithRowbounds(
			HubSupplierPicRuleCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubSupplierPicRuleMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubSupplierPicRule> selectByCriteria(HubSupplierPicRuleCriteria criteria) {
		return hubSupplierPicRuleMapper.selectByExample(criteria);
	}

	public HubSupplierPicRule selectByPrimaryKey(Long supplierPicRuleId) {
		return hubSupplierPicRuleMapper.selectByPrimaryKey(supplierPicRuleId);
	}

	public int updateByCriteriaSelective(HubSupplierPicRuleWithCriteria hubSupplierPicRuleWithCriteria) {
		return hubSupplierPicRuleMapper.updateByExampleSelective(hubSupplierPicRuleWithCriteria.getHubSupplierPicRule(), hubSupplierPicRuleWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubSupplierPicRuleWithCriteria hubSupplierPicRuleWithCriteria) {
		return hubSupplierPicRuleMapper.updateByExample(hubSupplierPicRuleWithCriteria.getHubSupplierPicRule(), hubSupplierPicRuleWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSupplierPicRule hubSupplierPicRule) {
		return hubSupplierPicRuleMapper.updateByPrimaryKeySelective(hubSupplierPicRule);
	}

	public int updateByPrimaryKey(HubSupplierPicRule hubSupplierPicRule) {
		return hubSupplierPicRuleMapper.updateByPrimaryKey(hubSupplierPicRule);
	}
}
