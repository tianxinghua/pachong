package com.shangpin.ephub.data.mysql.mapping.column.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.mapping.column.bean.HubSupplierColMappingCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.mapping.column.bean.HubSupplierColMappingWithCriteria;
import com.shangpin.ephub.data.mysql.mapping.column.po.HubSupplierColMapping;
import com.shangpin.ephub.data.mysql.mapping.column.po.HubSupplierColMappingCriteria;
import com.shangpin.ephub.data.mysql.mapping.column.service.HubSupplierColMappingService;
/**
 * <p>Title:HubSupplierColMappingController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:39:37
 */
@RestController
@RequestMapping("/hub-supplier-col-mapping")
public class HubSupplierColMappingController {

	@Autowired
	private HubSupplierColMappingService hubSupplierColMappingService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSupplierColMappingCriteria criteria){
    	return hubSupplierColMappingService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSupplierColMappingCriteria criteria){
    	return hubSupplierColMappingService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long supplierColMappingId){
    	return hubSupplierColMappingService.deleteByPrimaryKey(supplierColMappingId);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody HubSupplierColMapping hubSupplierColMapping){
    	hubSupplierColMappingService.insert(hubSupplierColMapping);
    	return hubSupplierColMapping.getSupplierColMappingId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody HubSupplierColMapping hubSupplierColMapping){
    	hubSupplierColMappingService.insertSelective(hubSupplierColMapping);
    	return hubSupplierColMapping.getSupplierColMappingId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSupplierColMapping> selectByCriteriaWithRowbounds(@RequestBody HubSupplierColMappingCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubSupplierColMappingService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSupplierColMapping> selectByCriteria(@RequestBody HubSupplierColMappingCriteria criteria){
    	return hubSupplierColMappingService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubSupplierColMapping selectByPrimaryKey(Long supplierColMappingId){
    	return hubSupplierColMappingService.selectByPrimaryKey(supplierColMappingId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSupplierColMappingWithCriteria hubSupplierColMappingWithCriteria){
    	return hubSupplierColMappingService.updateByCriteriaSelective(hubSupplierColMappingWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSupplierColMappingWithCriteria hubSupplierColMappingWithCriteria){
    	return hubSupplierColMappingService.updateByCriteria(hubSupplierColMappingWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierColMapping hubSupplierColMapping){
    	return hubSupplierColMappingService.updateByPrimaryKeySelective(hubSupplierColMapping);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSupplierColMapping hubSupplierColMapping){
    	return hubSupplierColMappingService.updateByPrimaryKey(hubSupplierColMapping);
    }
}
