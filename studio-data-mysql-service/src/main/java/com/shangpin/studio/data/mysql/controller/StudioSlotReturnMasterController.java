package com.shangpin.studio.data.mysql.controller;


import com.shangpin.studio.data.mysql.bean.StudioSlotReturnMasterCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioSlotReturnMasterWithCriteria;
import com.shangpin.studio.data.mysql.po.StudioSlotReturnMaster;
import com.shangpin.studio.data.mysql.po.StudioSlotReturnMasterCriteria;
import com.shangpin.studio.data.mysql.service.StudioSlotReturnMasterService;
import com.shangpin.studio.data.mysql.service.StudioSlotSpuSendDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *

 */
@RestController
@RequestMapping("/studio-slot-return-master")
public class StudioSlotReturnMasterController {

	@Autowired
	private StudioSlotReturnMasterService service;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody StudioSlotReturnMasterCriteria criteria){
    	return service.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody StudioSlotReturnMasterCriteria criteria){
    	return service.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long id){
    	return service.deleteByPrimaryKey(id);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody StudioSlotReturnMaster obj){
    	service.insert(obj);
    	return obj.getStudioSlotReturnMasterId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody StudioSlotReturnMaster obj){
    	service.insertSelective(obj);
		return obj.getStudioSlotReturnMasterId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<StudioSlotReturnMaster> selectByCriteriaWithRowbounds(@RequestBody StudioSlotReturnMasterCriteriaWithRowBounds criteriaWithRowBounds){
    	return service.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<StudioSlotReturnMaster> selectByCriteria(@RequestBody StudioSlotReturnMasterCriteria criteria){
    	return service.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public StudioSlotReturnMaster selectByPrimaryKey(Long id){
    	return service.selectByPrimaryKey(id);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody StudioSlotReturnMasterWithCriteria criteria){
    	return service.updateByCriteriaSelective(criteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody StudioSlotReturnMasterWithCriteria criteria){
    	return service.updateByCriteria(criteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody StudioSlotReturnMaster obj){
    	return service.updateByPrimaryKeySelective(obj);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody StudioSlotReturnMaster obj){
    	return service.updateByPrimaryKey(obj);
    }

	
}
