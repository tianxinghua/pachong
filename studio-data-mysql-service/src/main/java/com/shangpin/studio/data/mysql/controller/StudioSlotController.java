package com.shangpin.studio.data.mysql.controller;


import com.shangpin.studio.data.mysql.bean.StudioSlotCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioSlotWithCriteria;
import com.shangpin.studio.data.mysql.bean.StudioWithCriteria;
import com.shangpin.studio.data.mysql.po.StudioSlot;
import com.shangpin.studio.data.mysql.po.StudioSlotCriteria;

import com.shangpin.studio.data.mysql.service.StudioSlotService;
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
public class StudioSlotController {

	@Autowired
	private StudioSlotService service;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody StudioSlotCriteria criteria){
    	return service.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody StudioSlotCriteria criteria){
    	return service.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long skuPendingId){
    	return service.deleteByPrimaryKey(skuPendingId);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody StudioSlot studioSlot){
    	service.insert(studioSlot);
    	return studioSlot.getStudioSlotId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody StudioSlot studioSlot){
    	service.insertSelective(studioSlot);
		return studioSlot.getStudioSlotId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<StudioSlot> selectByCriteriaWithRowbounds(@RequestBody StudioSlotCriteriaWithRowBounds criteriaWithRowBounds){
    	return service.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<StudioSlot> selectByCriteria(@RequestBody StudioSlotCriteria criteria){
    	return service.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public StudioSlot selectByPrimaryKey(Long skuPendingId){
    	return service.selectByPrimaryKey(skuPendingId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody StudioSlotWithCriteria studioSlotWithCriteria){
    	return service.updateByCriteriaSelective(studioSlotWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody StudioSlotWithCriteria studioSlotWithCriteria){
    	return service.updateByCriteria(studioSlotWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody StudioSlot StudioSlot){
    	return service.updateByPrimaryKeySelective(StudioSlot);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody StudioSlot StudioSlot){
    	return service.updateByPrimaryKey(StudioSlot);
    }

	
}
