package com.shangpin.ephub.data.mysql.supplier.config.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.supplier.config.bean.HubSupplierConfigCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.supplier.config.bean.HubSupplierConfigWithCriteria;
import com.shangpin.ephub.data.mysql.supplier.config.po.HubSupplierConfig;
import com.shangpin.ephub.data.mysql.supplier.config.po.HubSupplierConfigCriteria;
import com.shangpin.ephub.data.mysql.supplier.config.servcie.HubSupplierConfigService;

/**
 * <p>Title:HubSupplierConfigController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:23:34
 */
@RestController
@RequestMapping("/hub-supplier-config")
public class HubSupplierConfigController {

	@Autowired
	private HubSupplierConfigService hubSupplierConfigService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSupplierConfigCriteria criteria){
    	return hubSupplierConfigService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSupplierConfigCriteria criteria){
    	return hubSupplierConfigService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long supplierConfigId){
    	return hubSupplierConfigService.deleteByPrimaryKey(supplierConfigId);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody HubSupplierConfig hubSupplierConfig){
    	hubSupplierConfigService.insert(hubSupplierConfig);
    	return hubSupplierConfig.getSupplierConfigId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody HubSupplierConfig hubSupplierConfig){
    	hubSupplierConfigService.insertSelective(hubSupplierConfig);
    	return hubSupplierConfig.getSupplierConfigId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSupplierConfig> selectByCriteriaWithRowbounds(@RequestBody HubSupplierConfigCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubSupplierConfigService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSupplierConfig> selectByCriteria(@RequestBody HubSupplierConfigCriteria criteria){
    	return hubSupplierConfigService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubSupplierConfig selectByPrimaryKey(Long supplierConfigId){
    	return hubSupplierConfigService.selectByPrimaryKey(supplierConfigId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSupplierConfigWithCriteria hubSupplierConfigWithCriteria){
    	return hubSupplierConfigService.updateByCriteriaSelective(hubSupplierConfigWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSupplierConfigWithCriteria hubSupplierConfigWithCriteria){
    	return hubSupplierConfigService.updateByCriteria(hubSupplierConfigWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierConfig hubBrandDic){
    	return hubSupplierConfigService.updateByPrimaryKeySelective(hubBrandDic);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSupplierConfig hubBrandDic){
    	return hubSupplierConfigService.updateByPrimaryKey(hubBrandDic);
    }
}
