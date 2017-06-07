package com.shangpin.studio.data.mysql.controller;


import com.shangpin.studio.data.mysql.bean.StudioSlotLogistictTrackCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioSlotLogistictTrackWithCriteria;
import com.shangpin.studio.data.mysql.po.StudioSlotLogistictTrack;
import com.shangpin.studio.data.mysql.po.StudioSlotLogistictTrackCriteria;
import com.shangpin.studio.data.mysql.service.StudioSlotLogistictTrackService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *

 */
@RestController
@RequestMapping("/studio-slot-logistict-track")
public class StudioSlotLogistictTrackController {

	@Autowired
	private StudioSlotLogistictTrackService service;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody StudioSlotLogistictTrackCriteria criteria){
    	return service.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody StudioSlotLogistictTrackCriteria criteria){
    	return service.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long id){
    	return service.deleteByPrimaryKey(id);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody StudioSlotLogistictTrack obj){
    	service.insert(obj);
    	return obj.getStudioSlotLogistictTrackId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody StudioSlotLogistictTrack obj){
    	service.insertSelective(obj);
		return obj.getStudioSlotLogistictTrackId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<StudioSlotLogistictTrack> selectByCriteriaWithRowbounds(@RequestBody StudioSlotLogistictTrackCriteriaWithRowBounds criteriaWithRowBounds){
    	return service.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<StudioSlotLogistictTrack> selectByCriteria(@RequestBody StudioSlotLogistictTrackCriteria criteria){
    	return service.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public StudioSlotLogistictTrack selectByPrimaryKey(Long id){
    	return service.selectByPrimaryKey(id);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody StudioSlotLogistictTrackWithCriteria withCriteria){
    	return service.updateByCriteriaSelective(withCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody StudioSlotLogistictTrackWithCriteria withCriteria){
    	return service.updateByCriteria(withCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody StudioSlotLogistictTrack obj){
    	return service.updateByPrimaryKeySelective(obj);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody StudioSlotLogistictTrack obj){
    	return service.updateByPrimaryKey(obj);
    }

	
}
