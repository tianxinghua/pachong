package com.shangpin.studio.data.mysql.controller;


import com.shangpin.studio.data.mysql.bean.StudioUserCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioUserWithCriteria;
import com.shangpin.studio.data.mysql.po.StudioUser;
import com.shangpin.studio.data.mysql.po.StudioUserCriteria;
import com.shangpin.studio.data.mysql.service.StudioUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *

 */
@RestController
@RequestMapping("/studio-slot")
public class StudioUserController {

	@Autowired
	private StudioUserService service;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody StudioUserCriteria criteria){
    	return service.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody StudioUserCriteria criteria){
    	return service.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long id){
    	return service.deleteByPrimaryKey(id);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody StudioUser obj){
    	service.insert(obj);
    	return obj.getStudioUserId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody StudioUser obj){
    	service.insertSelective(obj);
		return obj.getStudioUserId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<StudioUser> selectByCriteriaWithRowbounds(@RequestBody StudioUserCriteriaWithRowBounds criteriaWithRowBounds){
    	return service.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<StudioUser> selectByCriteria(@RequestBody StudioUserCriteria criteria){
    	return service.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public StudioUser selectByPrimaryKey(Long id){
    	return service.selectByPrimaryKey(id);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody StudioUserWithCriteria withCriteria){
    	return service.updateByCriteriaSelective(withCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody StudioUserWithCriteria withCriteria){
    	return service.updateByCriteria(withCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody StudioUser obj){
    	return service.updateByPrimaryKeySelective(obj);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody StudioUser obj){
    	return service.updateByPrimaryKey(obj);
    }

	
}
