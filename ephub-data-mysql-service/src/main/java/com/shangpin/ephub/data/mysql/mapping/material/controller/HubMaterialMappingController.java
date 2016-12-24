package com.shangpin.ephub.data.mysql.mapping.material.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.mapping.material.bean.HubMaterialMappingCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.mapping.material.bean.HubMaterialMappingWithCriteria;
import com.shangpin.ephub.data.mysql.mapping.material.po.HubMaterialMapping;
import com.shangpin.ephub.data.mysql.mapping.material.po.HubMaterialMappingCriteria;
import com.shangpin.ephub.data.mysql.mapping.material.service.HubMaterialMappingService;

/**
 * <p>Title:HubMaterialMappingController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月22日 上午11:52:02
 */
@RestController
@RequestMapping("/hub-material-mapping")
public class HubMaterialMappingController {

	@Autowired
	private HubMaterialMappingService hubMaterialMappingService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubMaterialMappingCriteria criteria){
    	return hubMaterialMappingService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubMaterialMappingCriteria criteria){
    	return hubMaterialMappingService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long materialMappingId){
    	return hubMaterialMappingService.deleteByPrimaryKey(materialMappingId);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody HubMaterialMapping hubMaterialMapping){
    	hubMaterialMappingService.insert(hubMaterialMapping);
    	return hubMaterialMapping.getMaterialMappingId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody HubMaterialMapping hubMaterialMapping){
    	hubMaterialMappingService.insertSelective(hubMaterialMapping);
    	return hubMaterialMapping.getMaterialMappingId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubMaterialMapping> selectByCriteriaWithRowbounds(@RequestBody HubMaterialMappingCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubMaterialMappingService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubMaterialMapping> selectByCriteria(@RequestBody HubMaterialMappingCriteria criteria){
    	return hubMaterialMappingService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubMaterialMapping selectByPrimaryKey(Long materialMappingId){
    	return hubMaterialMappingService.selectByPrimaryKey(materialMappingId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubMaterialMappingWithCriteria hubMaterialMappingWithCriteria){
    	return hubMaterialMappingService.updateByCriteriaSelective(hubMaterialMappingWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubMaterialMappingWithCriteria hubMaterialMappingWithCriteria){
    	return hubMaterialMappingService.updateByCriteria(hubMaterialMappingWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubMaterialMapping hubMaterialMapping){
    	return hubMaterialMappingService.updateByPrimaryKeySelective(hubMaterialMapping);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubMaterialMapping hubMaterialMapping){
    	return hubMaterialMappingService.updateByPrimaryKey(hubMaterialMapping);
    }
}
