package com.shangpin.studio.data.mysql.controller;


import com.shangpin.studio.data.mysql.bean.StudioCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioWithCriteria;
import com.shangpin.studio.data.mysql.po.Studio;
import com.shangpin.studio.data.mysql.po.StudioCriteria;
import com.shangpin.studio.data.mysql.service.StudioService;
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
@RequestMapping("/studio")
public class StudioController {

	@Autowired
	private StudioService studioService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody StudioCriteria criteria){
    	return studioService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody StudioCriteria criteria){
    	return studioService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long skuPendingId){
    	return studioService.deleteByPrimaryKey(skuPendingId);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody Studio studio){
    	studioService.insert(studio);
    	return studio.getStudioId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody Studio studio){
    	studioService.insertSelective(studio);
		return studio.getStudioId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<Studio> selectByCriteriaWithRowbounds(@RequestBody StudioCriteriaWithRowBounds criteriaWithRowBounds){
    	return studioService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<Studio> selectByCriteria(@RequestBody StudioCriteria criteria){
    	return studioService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public Studio selectByPrimaryKey(Long skuPendingId){
    	return studioService.selectByPrimaryKey(skuPendingId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody StudioWithCriteria studioWithCriteria){
    	return studioService.updateByCriteriaSelective(studioWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody StudioWithCriteria studioWithCriteria){
    	return studioService.updateByCriteria(studioWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody Studio Studio){
    	return studioService.updateByPrimaryKeySelective(Studio);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody Studio Studio){
    	return studioService.updateByPrimaryKey(Studio);
    }

	
}
