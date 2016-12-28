package com.shangpin.ephub.data.mysql.sku.pending.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.sku.pending.bean.HubSkuPendingCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.sku.pending.bean.HubSkuPendingWithCriteria;
import com.shangpin.ephub.data.mysql.sku.pending.po.HubSkuPending;
import com.shangpin.ephub.data.mysql.sku.pending.po.HubSkuPendingCriteria;
import com.shangpin.ephub.data.mysql.sku.pending.service.HubSkuPendingService;

/**
 * <p>Title:HubSkuPendingController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:12:44
 */
@RestController
@RequestMapping("/hub-sku-pending")
public class HubSkuPendingController {

	@Autowired
	private HubSkuPendingService hubSkuPendingService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSkuPendingCriteria criteria){
    	return hubSkuPendingService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSkuPendingCriteria criteria){
    	return hubSkuPendingService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long skuPendingId){
    	return hubSkuPendingService.deleteByPrimaryKey(skuPendingId);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody HubSkuPending hubSkuPending){
    	hubSkuPendingService.insert(hubSkuPending);
    	return hubSkuPending.getSkuPendingId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody HubSkuPending hubSkuPending){
    	hubSkuPendingService.insertSelective(hubSkuPending);
    	return hubSkuPending.getSkuPendingId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSkuPending> selectByCriteriaWithRowbounds(@RequestBody HubSkuPendingCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubSkuPendingService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSkuPending> selectByCriteria(@RequestBody HubSkuPendingCriteria criteria){
    	return hubSkuPendingService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubSkuPending selectByPrimaryKey(Long skuPendingId){
    	return hubSkuPendingService.selectByPrimaryKey(skuPendingId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSkuPendingWithCriteria hubSkuPendingWithCriteria){
    	return hubSkuPendingService.updateByCriteriaSelective(hubSkuPendingWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSkuPendingWithCriteria hubSkuPendingWithCriteria){
    	return hubSkuPendingService.updateByCriteria(hubSkuPendingWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSkuPending hubSkuPending){
    	return hubSkuPendingService.updateByPrimaryKeySelective(hubSkuPending);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSkuPending hubSkuPending){
    	return hubSkuPendingService.updateByPrimaryKey(hubSkuPending);
    }
}
