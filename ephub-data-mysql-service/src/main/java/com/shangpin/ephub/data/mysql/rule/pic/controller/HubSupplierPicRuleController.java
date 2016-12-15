package com.shangpin.ephub.data.mysql.rule.pic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.shangpin.ephub.data.mysql.rule.pic.bean.HubSupplierPicRuleCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.rule.pic.bean.HubSupplierPicRuleWithCriteria;
import com.shangpin.ephub.data.mysql.rule.pic.po.HubSupplierPicRule;
import com.shangpin.ephub.data.mysql.rule.pic.po.HubSupplierPicRuleCriteria;
import com.shangpin.ephub.data.mysql.rule.pic.service.HubSupplierPicRuleService;

/**
 * <p>Title:HubSupplierPicRuleController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:58:33
 */
@RestController
@RequestMapping("/hub-supplier-pic-rule")
public class HubSupplierPicRuleController {

	@Autowired
	private HubSupplierPicRuleService hubSupplierPicRuleService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSupplierPicRuleCriteria criteria){
    	return hubSupplierPicRuleService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSupplierPicRuleCriteria criteria){
    	return hubSupplierPicRuleService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long supplierPicRuleId){
    	return hubSupplierPicRuleService.deleteByPrimaryKey(supplierPicRuleId);
    }
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSupplierPicRule hubSupplierPicRule){
    	return hubSupplierPicRuleService.insert(hubSupplierPicRule);
    }
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSupplierPicRule hubSupplierPicRule){
    	return hubSupplierPicRuleService.insertSelective(hubSupplierPicRule);
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSupplierPicRule> selectByCriteriaWithRowbounds(@RequestBody HubSupplierPicRuleCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubSupplierPicRuleService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSupplierPicRule> selectByCriteria(@RequestBody HubSupplierPicRuleCriteria criteria){
    	return hubSupplierPicRuleService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubSupplierPicRule selectByPrimaryKey(Long supplierPicRuleId){
    	return hubSupplierPicRuleService.selectByPrimaryKey(supplierPicRuleId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSupplierPicRuleWithCriteria hubSupplierPicRuleWithCriteria){
    	return hubSupplierPicRuleService.updateByCriteriaSelective(hubSupplierPicRuleWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSupplierPicRuleWithCriteria hubSupplierPicRuleWithCriteria){
    	return hubSupplierPicRuleService.updateByCriteria(hubSupplierPicRuleWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierPicRule hubSupplierPicRule){
    	return hubSupplierPicRuleService.updateByPrimaryKeySelective(hubSupplierPicRule);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSupplierPicRule hubSupplierPicRule){
    	return hubSupplierPicRuleService.updateByPrimaryKey(hubSupplierPicRule);
    }
}
