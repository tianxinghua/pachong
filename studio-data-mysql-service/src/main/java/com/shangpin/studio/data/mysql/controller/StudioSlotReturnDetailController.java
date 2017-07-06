package com.shangpin.studio.data.mysql.controller;


import com.shangpin.studio.data.mysql.bean.StudioSlotReturnDetailCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioSlotReturnDetailWithCriteria;
import com.shangpin.studio.data.mysql.bean.StudioSlotReturnMasterCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioSlotReturnMasterWithCriteria;
import com.shangpin.studio.data.mysql.po.StudioSlotReturnDetail;
import com.shangpin.studio.data.mysql.po.StudioSlotReturnDetailCriteria;
import com.shangpin.studio.data.mysql.po.StudioSlotReturnDetail;
import com.shangpin.studio.data.mysql.po.StudioSlotReturnMasterCriteria;
import com.shangpin.studio.data.mysql.service.StudioSlotReturnDetailService;
import com.shangpin.studio.data.mysql.service.StudioSlotReturnMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *

 */
@RestController
@RequestMapping("/studio-slot-return-detail")
public class StudioSlotReturnDetailController {

	@Autowired
	private StudioSlotReturnDetailService service;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody StudioSlotReturnDetailCriteria criteria){
    	return service.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody StudioSlotReturnDetailCriteria criteria){
    	return service.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long id){
    	return service.deleteByPrimaryKey(id);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody StudioSlotReturnDetail obj){
    	service.insert(obj);
    	return obj.getStudioSlotReturnDetailId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody StudioSlotReturnDetail obj){
    	service.insertSelective(obj);
		return obj.getStudioSlotReturnDetailId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<StudioSlotReturnDetail> selectByCriteriaWithRowbounds(@RequestBody StudioSlotReturnDetailCriteriaWithRowBounds criteriaWithRowBounds){
    	return service.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<StudioSlotReturnDetail> selectByCriteria(@RequestBody StudioSlotReturnDetailCriteria criteria){
    	return service.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key/{id}")
    public StudioSlotReturnDetail selectByPrimaryKey(@PathVariable(name="id") Long id){

    	return service.selectByPrimaryKey(id);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody StudioSlotReturnDetailWithCriteria criteria){
    	return service.updateByCriteriaSelective(criteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody StudioSlotReturnDetailWithCriteria criteria){
    	return service.updateByCriteria(criteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody StudioSlotReturnDetail obj){
    	return service.updateByPrimaryKeySelective(obj);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody StudioSlotReturnDetail obj){
    	return service.updateByPrimaryKey(obj);
    }

	
}
