package com.shangpin.studio.data.mysql.controller;


import com.shangpin.studio.data.mysql.bean.StudioDicSupplierCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioDicSupplierWithCriteria;

import com.shangpin.studio.data.mysql.po.dic.StudioDicSupplier;
import com.shangpin.studio.data.mysql.po.dic.StudioDicSupplierCriteria;
import com.shangpin.studio.data.mysql.service.StudioDicSupplierService;

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
public class StudioDicSupplierController {

	@Autowired
	private StudioDicSupplierService service;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody StudioDicSupplierCriteria criteria){
    	return service.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody StudioDicSupplierCriteria criteria){
    	return service.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long id){
    	return service.deleteByPrimaryKey(id);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody StudioDicSupplier obj){
    	service.insert(obj);
    	return obj.getStudioDicSupplierId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody StudioDicSupplier obj){
    	service.insertSelective(obj);
		return obj.getStudioDicSupplierId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<StudioDicSupplier> selectByCriteriaWithRowbounds(@RequestBody StudioDicSupplierCriteriaWithRowBounds criteriaWithRowBounds){
    	return service.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<StudioDicSupplier> selectByCriteria(@RequestBody StudioDicSupplierCriteria criteria){
    	return service.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public StudioDicSupplier selectByPrimaryKey(Long id){
    	return service.selectByPrimaryKey(id);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody StudioDicSupplierWithCriteria withCriteria){
    	return service.updateByCriteriaSelective(withCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody StudioDicSupplierWithCriteria withCriteria){
    	return service.updateByCriteria(withCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody StudioDicSupplier obj){
    	return service.updateByPrimaryKeySelective(obj);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody StudioDicSupplier obj){
    	return service.updateByPrimaryKey(obj);
    }

	
}
