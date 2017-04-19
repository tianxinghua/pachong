package com.shangpin.ephub.data.mysql.mapping.value.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.mapping.value.bean.HubSupplierValueMappingCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.mapping.value.bean.HubSupplierValueMappingWithCriteria;
import com.shangpin.ephub.data.mysql.mapping.value.po.HubSupplierValueMapping;
import com.shangpin.ephub.data.mysql.mapping.value.po.HubSupplierValueMappingCriteria;
import com.shangpin.ephub.data.mysql.mapping.value.service.HubSupplierValueMappingService;
/**
 * <p>Title:HubSupplierValueMappingController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:47:53
 */
@RestController
@RequestMapping("/hub-supplier-value-mapping")
public class HubSupplierValueMappingController {

	@Autowired
	private HubSupplierValueMappingService hubSupplierValueMappingService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSupplierValueMappingCriteria criteria){
    	return hubSupplierValueMappingService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSupplierValueMappingCriteria criteria){
    	return hubSupplierValueMappingService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key/{valueMappingId}")
    public int deleteByPrimaryKey(@PathVariable("valueMappingId") Long valueMappingId){
    	return hubSupplierValueMappingService.deleteByPrimaryKey(valueMappingId);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody HubSupplierValueMapping hubSupplierValueMapping){
    	hubSupplierValueMappingService.insert(hubSupplierValueMapping);
    	return hubSupplierValueMapping.getHubSupplierValMappingId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody HubSupplierValueMapping hubSupplierValueMapping){
    	hubSupplierValueMappingService.insertSelective(hubSupplierValueMapping);
    	return hubSupplierValueMapping.getHubSupplierValMappingId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSupplierValueMapping> selectByCriteriaWithRowbounds(@RequestBody HubSupplierValueMappingCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubSupplierValueMappingService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSupplierValueMapping> selectByCriteria(@RequestBody HubSupplierValueMappingCriteria hubSupplierValueMappingCriteria){
    	return hubSupplierValueMappingService.selectByCriteria(hubSupplierValueMappingCriteria);
    }
	@RequestMapping(value = "/select-by-primary-key/{valueMappingId}")
    public HubSupplierValueMapping selectByPrimaryKey(@PathVariable("valueMappingId") Long valueMappingId){
    	return hubSupplierValueMappingService.selectByPrimaryKey(valueMappingId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSupplierValueMappingWithCriteria hubSupplierValueMappingWithCriteria){
    	return hubSupplierValueMappingService.updateByCriteriaSelective(hubSupplierValueMappingWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSupplierValueMappingWithCriteria hubSupplierValueMappingWithCriteria){
    	return hubSupplierValueMappingService.updateByCriteria(hubSupplierValueMappingWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierValueMapping hubSupplierValueMapping){
    	return hubSupplierValueMappingService.updateByPrimaryKeySelective(hubSupplierValueMapping);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSupplierValueMapping hubSupplierValueMapping){
    	return hubSupplierValueMappingService.updateByPrimaryKey(hubSupplierValueMapping);
    }
}
