package com.shangpin.studio.data.mysql.controller;


import com.shangpin.studio.data.mysql.bean.StudioSlotDefectiveSpuPicCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioSlotDefectiveSpuPicWithCriteria;
import com.shangpin.studio.data.mysql.po.StudioSlotDefectiveSpuPic;
import com.shangpin.studio.data.mysql.po.StudioSlotDefectiveSpuPicCriteria;
import com.shangpin.studio.data.mysql.service.StudioSlotDefectiveSpuPicService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *

 */
@RestController
@RequestMapping("/studio-slot-defective-spu-pic")
public class StudioSlotDefectiveSpuPicController {

	@Autowired
	private StudioSlotDefectiveSpuPicService service;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody StudioSlotDefectiveSpuPicCriteria criteria){
    	return service.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody StudioSlotDefectiveSpuPicCriteria criteria){
    	return service.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long id){
    	return service.deleteByPrimaryKey(id);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody StudioSlotDefectiveSpuPic obj){
    	service.insert(obj);
    	return obj.getStudioSlotDefectiveSpuPicId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody StudioSlotDefectiveSpuPic obj){
    	service.insertSelective(obj);
		return obj.getStudioSlotDefectiveSpuPicId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<StudioSlotDefectiveSpuPic> selectByCriteriaWithRowbounds(@RequestBody StudioSlotDefectiveSpuPicCriteriaWithRowBounds criteriaWithRowBounds){
    	return service.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<StudioSlotDefectiveSpuPic> selectByCriteria(@RequestBody StudioSlotDefectiveSpuPicCriteria criteria){
    	return service.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public StudioSlotDefectiveSpuPic selectByPrimaryKey(Long id){
    	return service.selectByPrimaryKey(id);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody StudioSlotDefectiveSpuPicWithCriteria withCriteria){
    	return service.updateByCriteriaSelective(withCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody StudioSlotDefectiveSpuPicWithCriteria withCriteria){
    	return service.updateByCriteria(withCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody StudioSlotDefectiveSpuPic obj){
    	return service.updateByPrimaryKeySelective(obj);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody StudioSlotDefectiveSpuPic obj){
    	return service.updateByPrimaryKey(obj);
    }

	
}
