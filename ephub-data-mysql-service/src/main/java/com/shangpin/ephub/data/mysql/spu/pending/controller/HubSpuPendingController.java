package com.shangpin.ephub.data.mysql.spu.pending.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.spu.pending.bean.HubSpuPendingCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.spu.pending.bean.HubSpuPendingWithCriteria;
import com.shangpin.ephub.data.mysql.spu.pending.po.HubSpuPending;
import com.shangpin.ephub.data.mysql.spu.pending.po.HubSpuPendingCriteria;
import com.shangpin.ephub.data.mysql.spu.pending.service.HubSpuPendingService;
/**
 * <p>Title:HubSpuPendingController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:19:51
 */
@RestController
@RequestMapping("/hub-spu-pending")
public class HubSpuPendingController {

	@Autowired
	private HubSpuPendingService hubSpuPendingService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSpuPendingCriteria criteria){
    	return hubSpuPendingService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSpuPendingCriteria criteria){
    	return hubSpuPendingService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long spuPendingId){
    	return hubSpuPendingService.deleteByPrimaryKey(spuPendingId);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody HubSpuPending hubSpuPending){
    	 hubSpuPendingService.insert(hubSpuPending);
    	 return hubSpuPending.getSpuPendingId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody HubSpuPending hubSpuPending){
    	 hubSpuPendingService.insertSelective(hubSpuPending);
    	 return hubSpuPending.getSpuPendingId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSpuPending> selectByCriteriaWithRowbounds(@RequestBody HubSpuPendingCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubSpuPendingService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSpuPending> selectByCriteria(@RequestBody HubSpuPendingCriteria criteria){
    	return hubSpuPendingService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubSpuPending selectByPrimaryKey(Long spuPendingId){
    	return hubSpuPendingService.selectByPrimaryKey(spuPendingId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSpuPendingWithCriteria hubSpuPendingWithCriteria){
    	return hubSpuPendingService.updateByCriteriaSelective(hubSpuPendingWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSpuPendingWithCriteria hubSpuPendingWithCriteria){
    	return hubSpuPendingService.updateByCriteria(hubSpuPendingWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSpuPending hubSpuPending){
    	return hubSpuPendingService.updateByPrimaryKeySelective(hubSpuPending);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSpuPending hubSpuPending){
    	return hubSpuPendingService.updateByPrimaryKey(hubSpuPending);
    }


	@RequestMapping(value = "/count-distinct-brandno-and-spumodel-by-criteria")
	public int countDistinctBrandNoAndSpuModelByCriteria(@RequestBody HubSpuPendingCriteria criteria){
		return hubSpuPendingService.countDistinctBrandNoAndSpuModelByCriteria(criteria);
	}
}
