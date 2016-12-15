package com.shangpin.ephub.data.mysql.mapping.value.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long valueMappingId){
    	return hubSupplierValueMappingService.deleteByPrimaryKey(valueMappingId);
    }
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSupplierValueMapping hubSupplierValueMapping){
    	return hubSupplierValueMappingService.insert(hubSupplierValueMapping);
    }
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSupplierValueMapping hubSupplierValueMapping){
    	return hubSupplierValueMappingService.insertSelective(hubSupplierValueMapping);
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSupplierValueMapping> selectByCriteriaWithRowbounds(@RequestBody HubSupplierValueMappingCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubSupplierValueMappingService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSupplierValueMapping> selectByCriteria(@RequestBody HubSupplierValueMappingCriteria criteria){
    	return hubSupplierValueMappingService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubSupplierValueMapping selectByPrimaryKey(Long valueMappingId){
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
