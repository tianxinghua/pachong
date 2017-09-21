package com.shangpin.ephub.data.mysql.sp.shippingpolicy.controller;




import com.shangpin.ephub.data.mysql.sp.shippingpolicy.bean.SpShippingPolicyCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.sp.shippingpolicy.bean.SpShippingPolicyWithCriteria;
import com.shangpin.ephub.data.mysql.sp.shippingpolicy.po.SpShippingPolicy;
import com.shangpin.ephub.data.mysql.sp.shippingpolicy.po.SpShippingPolicyCriteria;
import com.shangpin.ephub.data.mysql.sp.shippingpolicy.service.SpShippingPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *

 */
@RestController
@RequestMapping("/sp-shipping-policy")
public class SpShippingPolicyController {

	@Autowired
	private SpShippingPolicyService service;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody SpShippingPolicyCriteria criteria){
    	return service.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody SpShippingPolicyCriteria criteria){
    	return service.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long id){
    	return service.deleteByPrimaryKey(id);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody SpShippingPolicy obj){
    	service.insert(obj);
    	return obj.getId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody SpShippingPolicy obj){
    	service.insertSelective(obj);
		return obj.getId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<SpShippingPolicy> selectByCriteriaWithRowbounds(@RequestBody SpShippingPolicyCriteriaWithRowBounds criteriaWithRowBounds){
    	return service.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<SpShippingPolicy> selectByCriteria(@RequestBody SpShippingPolicyCriteria criteria){
    	return service.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public SpShippingPolicy selectByPrimaryKey(Long id){
    	return service.selectByPrimaryKey(id);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody SpShippingPolicyWithCriteria withCriteria){
    	return service.updateByCriteriaSelective(withCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody SpShippingPolicyWithCriteria withCriteria){
    	return service.updateByCriteria(withCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody SpShippingPolicy obj){
    	return service.updateByPrimaryKeySelective(obj);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody SpShippingPolicy obj){
    	return service.updateByPrimaryKey(obj);
    }

	
}
