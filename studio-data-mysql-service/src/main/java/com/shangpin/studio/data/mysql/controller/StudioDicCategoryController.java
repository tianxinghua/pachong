package com.shangpin.studio.data.mysql.controller;


import com.shangpin.studio.data.mysql.bean.StudioDicCategoryCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioDicCategoryWithCriteria;

import com.shangpin.studio.data.mysql.po.dic.StudioDicCategory;
import com.shangpin.studio.data.mysql.po.dic.StudioDicCategoryCriteria;
import com.shangpin.studio.data.mysql.service.StudioDicCategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *

 */
@RestController
@RequestMapping("/studio-dic-category")
public class StudioDicCategoryController {

	@Autowired
	private StudioDicCategoryService service;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody StudioDicCategoryCriteria criteria){
    	return service.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody StudioDicCategoryCriteria criteria){
    	return service.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long id){
    	return service.deleteByPrimaryKey(id);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody StudioDicCategory obj){
    	service.insert(obj);
    	return obj.getStudioDicCategoryId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody StudioDicCategory obj){
    	service.insertSelective(obj);
		return obj.getStudioDicCategoryId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<StudioDicCategory> selectByCriteriaWithRowbounds(@RequestBody StudioDicCategoryCriteriaWithRowBounds criteriaWithRowBounds){
    	return service.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<StudioDicCategory> selectByCriteria(@RequestBody StudioDicCategoryCriteria criteria){
    	return service.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public StudioDicCategory selectByPrimaryKey(Long id){
    	return service.selectByPrimaryKey(id);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody StudioDicCategoryWithCriteria withCriteria){
    	return service.updateByCriteriaSelective(withCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody StudioDicCategoryWithCriteria withCriteria){
    	return service.updateByCriteria(withCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody StudioDicCategory obj){
    	return service.updateByPrimaryKeySelective(obj);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody StudioDicCategory obj){
    	return service.updateByPrimaryKey(obj);
    }

	
}
