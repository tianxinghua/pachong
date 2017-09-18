package com.shangpin.ephub.data.mysql.sp.spshippingpolicydetail.controller;



import com.shangpin.ephub.data.mysql.sp.spshippingpolicydetail.bean.SpShippingPolicyDetailCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.sp.spshippingpolicydetail.bean.SpShippingPolicyDetailWithCriteria;
import com.shangpin.ephub.data.mysql.sp.spshippingpolicydetail.po.SpShippingPolicyDetail;
import com.shangpin.ephub.data.mysql.sp.spshippingpolicydetail.po.SpShippingPolicyDetailCriteria;
import com.shangpin.ephub.data.mysql.sp.spshippingpolicydetail.service.SpShippingPolicyDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *

 */
@RestController
@RequestMapping("/sp-shipping-policy-detail")
public class SpShippingPolicyDetailController {

	@Autowired
	private SpShippingPolicyDetailService service;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody SpShippingPolicyDetailCriteria criteria){
    	return service.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody SpShippingPolicyDetailCriteria criteria){
    	return service.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long id){
    	return service.deleteByPrimaryKey(id);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody SpShippingPolicyDetail obj){
    	service.insert(obj);
    	return obj.getId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody SpShippingPolicyDetail obj){
    	service.insertSelective(obj);
		return obj.getId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<SpShippingPolicyDetail> selectByCriteriaWithRowbounds(@RequestBody SpShippingPolicyDetailCriteriaWithRowBounds criteriaWithRowBounds){
    	return service.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<SpShippingPolicyDetail> selectByCriteria(@RequestBody SpShippingPolicyDetailCriteria criteria){
    	return service.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public SpShippingPolicyDetail selectByPrimaryKey(Long id){
    	return service.selectByPrimaryKey(id);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody SpShippingPolicyDetailWithCriteria withCriteria){
    	return service.updateByCriteriaSelective(withCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody SpShippingPolicyDetailWithCriteria withCriteria){
    	return service.updateByCriteria(withCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody SpShippingPolicyDetail obj){
    	return service.updateByPrimaryKeySelective(obj);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody SpShippingPolicyDetail obj){
    	return service.updateByPrimaryKey(obj);
    }

	
}
