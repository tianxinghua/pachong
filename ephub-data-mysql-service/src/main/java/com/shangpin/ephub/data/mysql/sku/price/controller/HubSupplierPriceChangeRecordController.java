package com.shangpin.ephub.data.mysql.sku.price.controller;

import com.shangpin.ephub.data.mysql.sku.price.bean.HubSupplierPriceChangeRecordWithCriteria;
import com.shangpin.ephub.data.mysql.sku.price.bean.HubSupplierPriceChangeRecordWithRowBounds;
import com.shangpin.ephub.data.mysql.sku.price.po.HubSupplierPriceChangeRecord;
import com.shangpin.ephub.data.mysql.sku.price.po.HubSupplierPriceChangeRecordCriteria;
import com.shangpin.ephub.data.mysql.sku.price.service.HubSupplierPriceChangeRecordService;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>Title:HubSupplierPriceChangeRecordController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:15:43
 */
@RestController
@RequestMapping("/hub-supplier-price")
public class HubSupplierPriceChangeRecordController {

	@Autowired
	private HubSupplierPriceChangeRecordService hubSupplierPriceChangeRecordService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSupplierPriceChangeRecordCriteria criteria){
    	return hubSupplierPriceChangeRecordService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSupplierPriceChangeRecordCriteria criteria){
    	return hubSupplierPriceChangeRecordService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long supplierPriceChangeRecordId){
    	return hubSupplierPriceChangeRecordService.deleteByPrimaryKey(supplierPriceChangeRecordId);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody HubSupplierPriceChangeRecord hubSupplierPriceChangeRecord){
    	hubSupplierPriceChangeRecordService.insert(hubSupplierPriceChangeRecord);
    	return hubSupplierPriceChangeRecord.getSupplierPriceChangeRecordId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody HubSupplierPriceChangeRecord hubSupplierPriceChangeRecord){
    	hubSupplierPriceChangeRecordService.insertSelective(hubSupplierPriceChangeRecord);
    	return hubSupplierPriceChangeRecord.getSupplierPriceChangeRecordId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSupplierPriceChangeRecord> selectByCriteriaWithRowbounds(@RequestBody HubSupplierPriceChangeRecordWithRowBounds criteriaWithRowBounds){
    	return hubSupplierPriceChangeRecordService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSupplierPriceChangeRecord> selectByCriteria(@RequestBody HubSupplierPriceChangeRecordCriteria criteria){
    	return hubSupplierPriceChangeRecordService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key/{supplierPriceChangeRecordId}")
    public HubSupplierPriceChangeRecord selectByPrimaryKey(@PathVariable(value = "supplierPriceChangeRecordId") Long supplierPriceChangeRecordId){
    	return hubSupplierPriceChangeRecordService.selectByPrimaryKey(supplierPriceChangeRecordId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSupplierPriceChangeRecordWithCriteria hubSupplierPriceChangeRecordWithCriteria){
    	return hubSupplierPriceChangeRecordService.updateByCriteriaSelective(hubSupplierPriceChangeRecordWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSupplierPriceChangeRecordWithCriteria hubSupplierPriceChangeRecordWithCriteria){
    	return hubSupplierPriceChangeRecordService.updateByCriteria(hubSupplierPriceChangeRecordWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierPriceChangeRecord hubSupplierPriceChangeRecord){
    	return hubSupplierPriceChangeRecordService.updateByPrimaryKeySelective(hubSupplierPriceChangeRecord);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSupplierPriceChangeRecord hubSupplierPriceChangeRecord){
    	return hubSupplierPriceChangeRecordService.updateByPrimaryKey(hubSupplierPriceChangeRecord);
    }
}
