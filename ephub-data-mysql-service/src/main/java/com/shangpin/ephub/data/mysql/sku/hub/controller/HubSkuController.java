package com.shangpin.ephub.data.mysql.sku.hub.controller;

import java.util.List;

import com.shangpin.ephub.data.mysql.product.common.HubSpuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.sku.hub.bean.HubSkuCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.sku.hub.bean.HubSkuWithCriteria;
import com.shangpin.ephub.data.mysql.sku.hub.po.HubSku;
import com.shangpin.ephub.data.mysql.sku.hub.po.HubSkuCriteria;
import com.shangpin.ephub.data.mysql.sku.hub.service.HubSkuService;

/**
 * <p>Title:HubSkuController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:08:37
 */
@RestController
@RequestMapping("/hub-sku")
public class HubSkuController {

	@Autowired
	private HubSkuService hubSkuService;

	@Autowired
	HubSpuUtil hubSpuUtil;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSkuCriteria criteria){
    	return hubSkuService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSkuCriteria criteria){
    	return hubSkuService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long skuId){
    	return hubSkuService.deleteByPrimaryKey(skuId);
    }
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSku hubSku){
    	return hubSkuService.insert(hubSku);
    }
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSku hubSku){
    	return hubSkuService.insertSelective(hubSku);
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSku> selectByCriteriaWithRowbounds(@RequestBody HubSkuCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubSkuService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSku> selectByCriteria(@RequestBody HubSkuCriteria criteria){
    	return hubSkuService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubSku selectByPrimaryKey(Long skuId){
    	return hubSkuService.selectByPrimaryKey(skuId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSkuWithCriteria hubSkuWithCriteria){
    	return hubSkuService.updateByCriteriaSelective(hubSkuWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSkuWithCriteria hubSkuWithCriteria){
    	return hubSkuService.updateByCriteria(hubSkuWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSku hubSku){
    	return hubSkuService.updateByPrimaryKeySelective(hubSku);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSku hubSku){
    	return hubSkuService.updateByPrimaryKey(hubSku);
    }

	@RequestMapping(value = "/get-skuno")
	public String createSkuNo(@RequestParam(value = "spuno")  String  spuno){
		return   hubSpuUtil.createHubSkuNo(spuno,1);
	}
}
