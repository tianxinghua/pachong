package com.shangpin.ephub.data.mysql.mapping.sku.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.mapping.sku.bean.HubSkuSupplierMappingCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.mapping.sku.bean.HubSkuSupplierMappingWithCriteria;
import com.shangpin.ephub.data.mysql.mapping.sku.po.HubSkuSupplierMapping;
import com.shangpin.ephub.data.mysql.mapping.sku.po.HubSkuSupplierMappingCriteria;
import com.shangpin.ephub.data.mysql.mapping.sku.service.HubSkuSupplierMappingService;

/**
 * <p>Title:HubSkuSupplierMappingController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:42:50
 */
@RestController
@RequestMapping("/hub-sku-supplier-mapping")
public class HubSkuSupplierMappingController {

	@Autowired
	private HubSkuSupplierMappingService hubSkuSupplierMappingService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSkuSupplierMappingCriteria criteria){
    	return hubSkuSupplierMappingService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSkuSupplierMappingCriteria criteria){
    	return hubSkuSupplierMappingService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long skuSupplierMappingId){
    	return hubSkuSupplierMappingService.deleteByPrimaryKey(skuSupplierMappingId);
    }
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSkuSupplierMapping hubSkuSupplierMapping){
    	return hubSkuSupplierMappingService.insert(hubSkuSupplierMapping);
    }
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSkuSupplierMapping hubSkuSupplierMapping){
    	return hubSkuSupplierMappingService.insertSelective(hubSkuSupplierMapping);
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSkuSupplierMapping> selectByCriteriaWithRowbounds(@RequestBody HubSkuSupplierMappingCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubSkuSupplierMappingService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSkuSupplierMapping> selectByCriteria(@RequestBody HubSkuSupplierMappingCriteria criteria){
    	return hubSkuSupplierMappingService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubSkuSupplierMapping selectByPrimaryKey(Long skuSupplierMappingId){
    	return hubSkuSupplierMappingService.selectByPrimaryKey(skuSupplierMappingId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSkuSupplierMappingWithCriteria hubSkuSupplierMappingWithCriteria){
    	return hubSkuSupplierMappingService.updateByCriteriaSelective(hubSkuSupplierMappingWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSkuSupplierMappingWithCriteria hubSkuSupplierMappingWithCriteria){
    	return hubSkuSupplierMappingService.updateByCriteria(hubSkuSupplierMappingWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSkuSupplierMapping hubSkuSupplierMapping){
    	return hubSkuSupplierMappingService.updateByPrimaryKeySelective(hubSkuSupplierMapping);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSkuSupplierMapping hubSkuSupplierMapping){
    	return hubSkuSupplierMappingService.updateByPrimaryKey(hubSkuSupplierMapping);
    }
}
