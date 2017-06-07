package com.shangpin.studio.data.mysql.controller;


import com.shangpin.studio.data.mysql.bean.StudioSlotDefectiveSpuCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioSlotDefectiveSpuWithCriteria;
import com.shangpin.studio.data.mysql.po.StudioSlotDefectiveSpu;
import com.shangpin.studio.data.mysql.po.StudioSlotDefectiveSpuCriteria;
import com.shangpin.studio.data.mysql.service.StudioSlotDefectiveSpuService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *

 */
@RestController
@RequestMapping("/studio-slot-defective-spu")
public class StudioSlotDefectiveSpuController {

	@Autowired
	private StudioSlotDefectiveSpuService service;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody StudioSlotDefectiveSpuCriteria criteria){
    	return service.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody StudioSlotDefectiveSpuCriteria criteria){
    	return service.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long id){
    	return service.deleteByPrimaryKey(id);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody StudioSlotDefectiveSpu obj){
    	service.insert(obj);
    	return obj.getStudioSlotDefectiveSpuId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody StudioSlotDefectiveSpu obj){
    	service.insertSelective(obj);
		return obj.getStudioSlotDefectiveSpuId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<StudioSlotDefectiveSpu> selectByCriteriaWithRowbounds(@RequestBody StudioSlotDefectiveSpuCriteriaWithRowBounds criteriaWithRowBounds){
    	return service.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<StudioSlotDefectiveSpu> selectByCriteria(@RequestBody StudioSlotDefectiveSpuCriteria criteria){
    	return service.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public StudioSlotDefectiveSpu selectByPrimaryKey(Long id){
    	return service.selectByPrimaryKey(id);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody StudioSlotDefectiveSpuWithCriteria withCriteria){
    	return service.updateByCriteriaSelective(withCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody StudioSlotDefectiveSpuWithCriteria withCriteria){
    	return service.updateByCriteria(withCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody StudioSlotDefectiveSpu obj){
    	return service.updateByPrimaryKeySelective(obj);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody StudioSlotDefectiveSpu obj){
    	return service.updateByPrimaryKey(obj);
    }

	
}
