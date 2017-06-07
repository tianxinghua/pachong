package com.shangpin.studio.data.mysql.controller;


import com.shangpin.studio.data.mysql.bean.StudioDicCalendarCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioDicCalendarWithCriteria;

import com.shangpin.studio.data.mysql.po.dic.StudioDicCalendar;
import com.shangpin.studio.data.mysql.po.dic.StudioDicCalendarCriteria;
import com.shangpin.studio.data.mysql.service.StudioDicCalendarService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *

 */
@RestController
@RequestMapping("/studio-dic-calendar")
public class StudioDicCalendarController {

	@Autowired
	private StudioDicCalendarService service;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody StudioDicCalendarCriteria criteria){
    	return service.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody StudioDicCalendarCriteria criteria){
    	return service.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long id){
    	return service.deleteByPrimaryKey(id);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody StudioDicCalendar obj){
    	service.insert(obj);
    	return obj.getStudioDicCalendarId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody StudioDicCalendar obj){
    	service.insertSelective(obj);
		return obj.getStudioDicCalendarId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<StudioDicCalendar> selectByCriteriaWithRowbounds(@RequestBody StudioDicCalendarCriteriaWithRowBounds criteriaWithRowBounds){
    	return service.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<StudioDicCalendar> selectByCriteria(@RequestBody StudioDicCalendarCriteria criteria){
    	return service.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public StudioDicCalendar selectByPrimaryKey(Long id){
    	return service.selectByPrimaryKey(id);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody StudioDicCalendarWithCriteria withCriteria){
    	return service.updateByCriteriaSelective(withCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody StudioDicCalendarWithCriteria withCriteria){
    	return service.updateByCriteria(withCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody StudioDicCalendar obj){
    	return service.updateByPrimaryKeySelective(obj);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody StudioDicCalendar obj){
    	return service.updateByPrimaryKey(obj);
    }

	
}
