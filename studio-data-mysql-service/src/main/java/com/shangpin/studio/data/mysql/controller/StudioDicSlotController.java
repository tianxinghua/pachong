package com.shangpin.studio.data.mysql.controller;


import com.shangpin.studio.data.mysql.bean.StudioDicSlotCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioDicSlotWithCriteria;

import com.shangpin.studio.data.mysql.po.dic.StudioDicSlot;
import com.shangpin.studio.data.mysql.po.dic.StudioDicSlotCriteria;
import com.shangpin.studio.data.mysql.service.StudioDicSlotService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *

 */
@RestController
@RequestMapping("/studio-dic-slot")
public class StudioDicSlotController {

	@Autowired
	private StudioDicSlotService service;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody StudioDicSlotCriteria criteria){
    	return service.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody StudioDicSlotCriteria criteria){
    	return service.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long id){
    	return service.deleteByPrimaryKey(id);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody StudioDicSlot obj){
    	service.insert(obj);
    	return obj.getStudioDicSlotId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody StudioDicSlot obj){
    	service.insertSelective(obj);
		return obj.getStudioDicSlotId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<StudioDicSlot> selectByCriteriaWithRowbounds(@RequestBody StudioDicSlotCriteriaWithRowBounds criteriaWithRowBounds){
    	return service.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<StudioDicSlot> selectByCriteria(@RequestBody StudioDicSlotCriteria criteria){
    	return service.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public StudioDicSlot selectByPrimaryKey(Long id){
    	return service.selectByPrimaryKey(id);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody StudioDicSlotWithCriteria withCriteria){
    	return service.updateByCriteriaSelective(withCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody StudioDicSlotWithCriteria withCriteria){
    	return service.updateByCriteria(withCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody StudioDicSlot obj){
    	return service.updateByPrimaryKeySelective(obj);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody StudioDicSlot obj){
    	return service.updateByPrimaryKey(obj);
    }

	
}
