package com.shangpin.ephub.data.mysql.sku.supplier.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.sku.supplier.bean.HubSupplierSkuCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.sku.supplier.bean.HubSupplierSkuWithCriteria;
import com.shangpin.ephub.data.mysql.sku.supplier.po.HubSupplierSku;
import com.shangpin.ephub.data.mysql.sku.supplier.po.HubSupplierSkuCriteria;
import com.shangpin.ephub.data.mysql.sku.supplier.service.HubSupplierSkuService;

/**
 * <p>Title:HubSupplierSkuController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:15:43
 */
@RestController
@RequestMapping("/hub-supplier-sku")
public class HubSupplierSkuController {

	@Autowired
	private HubSupplierSkuService hubSupplierSkuService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSupplierSkuCriteria criteria){
    	return hubSupplierSkuService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSupplierSkuCriteria criteria){
    	return hubSupplierSkuService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long supplierSkuId){
    	return hubSupplierSkuService.deleteByPrimaryKey(supplierSkuId);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody HubSupplierSku hubSupplierSku){
    	hubSupplierSkuService.insert(hubSupplierSku);
    	return hubSupplierSku.getSupplierSkuId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody HubSupplierSku hubSupplierSku){
    	hubSupplierSkuService.insertSelective(hubSupplierSku);
    	return hubSupplierSku.getSupplierSkuId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSupplierSku> selectByCriteriaWithRowbounds(@RequestBody HubSupplierSkuCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubSupplierSkuService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSupplierSku> selectByCriteria(@RequestBody HubSupplierSkuCriteria criteria){
    	return hubSupplierSkuService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key/{supplierSkuId}")
    public HubSupplierSku selectByPrimaryKey(@PathVariable(value = "supplierSkuId") Long supplierSkuId){
    	return hubSupplierSkuService.selectByPrimaryKey(supplierSkuId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSupplierSkuWithCriteria hubSupplierSkuWithCriteria){
    	return hubSupplierSkuService.updateByCriteriaSelective(hubSupplierSkuWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSupplierSkuWithCriteria hubSupplierSkuWithCriteria){
    	return hubSupplierSkuService.updateByCriteria(hubSupplierSkuWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierSku hubSupplierSku){
    	return hubSupplierSkuService.updateByPrimaryKeySelective(hubSupplierSku);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSupplierSku hubSupplierSku){
    	return hubSupplierSkuService.updateByPrimaryKey(hubSupplierSku);
    }
}
