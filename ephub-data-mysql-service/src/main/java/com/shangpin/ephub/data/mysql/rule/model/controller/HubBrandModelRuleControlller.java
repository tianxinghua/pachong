package com.shangpin.ephub.data.mysql.rule.model.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.rule.model.bean.HubBrandModelRuleCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.rule.model.bean.HubBrandModelRuleWithCriteria;
import com.shangpin.ephub.data.mysql.rule.model.po.HubBrandModelRule;
import com.shangpin.ephub.data.mysql.rule.model.po.HubBrandModelRuleCriteria;
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
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubBrandModelRuleCriteria criteria){
    	return hubBrandModelRuleService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubBrandModelRuleCriteria criteria){
    	return hubBrandModelRuleService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long brandModelRuleId){
    	return hubBrandModelRuleService.deleteByPrimaryKey(brandModelRuleId);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody HubBrandModelRule hubBrandModelRule){
    	hubBrandModelRuleService.insert(hubBrandModelRule);
    	return hubBrandModelRule.getBrandModelRuleId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody HubBrandModelRule hubBrandModelRule){
    	hubBrandModelRuleService.insertSelective(hubBrandModelRule);
    	return hubBrandModelRule.getBrandModelRuleId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubBrandModelRule> selectByCriteriaWithRowbounds(@RequestBody HubBrandModelRuleCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubBrandModelRuleService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubBrandModelRule> selectByCriteria(@RequestBody HubBrandModelRuleCriteria criteria){
    	return hubBrandModelRuleService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubBrandModelRule selectByPrimaryKey(Long brandModelRuleId){
    	return hubBrandModelRuleService.selectByPrimaryKey(brandModelRuleId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubBrandModelRuleWithCriteria hubBrandModelRuleWithCriteria){
    	return hubBrandModelRuleService.updateByCriteriaSelective(hubBrandModelRuleWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubBrandModelRuleWithCriteria hubBrandModelRuleWithCriteria){
    	return hubBrandModelRuleService.updateByCriteria(hubBrandModelRuleWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubBrandModelRule hubBrandModelRule){
    	return hubBrandModelRuleService.updateByPrimaryKeySelective(hubBrandModelRule);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubBrandModelRule hubBrandModelRule){
    	return hubBrandModelRuleService.updateByPrimaryKey(hubBrandModelRule);
    }
}
