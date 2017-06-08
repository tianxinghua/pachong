package com.shangpin.ephub.data.mysql.slot.appendix.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shangpin.ephub.data.mysql.slot.appendix.bean.HubSlotSpuAppendixCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.slot.appendix.bean.HubSlotSpuAppendixWithCriteria;
import com.shangpin.ephub.data.mysql.slot.appendix.po.HubSlotSpuAppendix;
import com.shangpin.ephub.data.mysql.slot.appendix.po.HubSlotSpuAppendixCriteria;
import com.shangpin.ephub.data.mysql.slot.appendix.service.HubSlotSpuAppendixService;

public class HubSlotSpuAppendixController {
	@Autowired
	private HubSlotSpuAppendixService hubSlotSpuAppendixService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSlotSpuAppendixCriteria criteria){
    	return hubSlotSpuAppendixService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSlotSpuAppendixCriteria criteria){
    	return hubSlotSpuAppendixService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long skuId){
    	return hubSlotSpuAppendixService.deleteByPrimaryKey(skuId);
    }
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSlotSpuAppendix HubSlotSpuAppendix){
    	return hubSlotSpuAppendixService.insert(HubSlotSpuAppendix);
    }
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSlotSpuAppendix HubSlotSpuAppendix){
    	return hubSlotSpuAppendixService.insertSelective(HubSlotSpuAppendix);
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSlotSpuAppendix> selectByCriteriaWithRowbounds(@RequestBody HubSlotSpuAppendixCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubSlotSpuAppendixService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSlotSpuAppendix> selectByCriteria(@RequestBody HubSlotSpuAppendixCriteria criteria){
    	return hubSlotSpuAppendixService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key/{skuId}")
    public HubSlotSpuAppendix selectByPrimaryKey(@PathVariable(value = "skuId") Long skuId){
    	return hubSlotSpuAppendixService.selectByPrimaryKey(skuId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSlotSpuAppendixWithCriteria HubSlotSpuAppendixWithCriteria){
    	return hubSlotSpuAppendixService.updateByCriteriaSelective(HubSlotSpuAppendixWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSlotSpuAppendixWithCriteria HubSlotSpuAppendixWithCriteria){
    	return hubSlotSpuAppendixService.updateByCriteria(HubSlotSpuAppendixWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSlotSpuAppendix HubSlotSpuAppendix){
    	return hubSlotSpuAppendixService.updateByPrimaryKeySelective(HubSlotSpuAppendix);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSlotSpuAppendix HubSlotSpuAppendix){
    	return hubSlotSpuAppendixService.updateByPrimaryKey(HubSlotSpuAppendix);
    }
}
