package com.shangpin.ephub.data.mysql.spu.hub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.product.common.HubSpuUtil;
import com.shangpin.ephub.data.mysql.spu.hub.bean.HubSpuCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.spu.hub.bean.HubSpuWithCriteria;
import com.shangpin.ephub.data.mysql.spu.hub.po.HubSpu;
import com.shangpin.ephub.data.mysql.spu.hub.po.HubSpuCriteria;
import com.shangpin.ephub.data.mysql.spu.hub.service.HubSpuService;
import com.shangpin.ephub.data.mysql.spu.supplier.po.HubSupplierSpuQureyDto;
/**
 * <p>Title:HubSpuController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:18:04
 */
@RestController
@RequestMapping("/hub-spu")
public class HubSpuController {

	@Autowired
	private HubSpuService hubSpuService;

	@Autowired
	HubSpuUtil hubSpuUtil;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSpuCriteria criteria){
    	return hubSpuService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSpuCriteria criteria){
    	return hubSpuService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long spuId){
    	return hubSpuService.deleteByPrimaryKey(spuId);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody HubSpu hubSpu){
    	hubSpuService.insert(hubSpu);
    	return hubSpu.getSpuId(); 
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody HubSpu hubSpu){
    	hubSpuService.insertSelective(hubSpu);
    	return hubSpu.getSpuId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSpu> selectByCriteriaWithRowbounds(@RequestBody HubSpuCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubSpuService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-brand")
    public List<HubSpu> selectByBrand(@RequestBody HubSupplierSpuQureyDto dto){
    	return hubSpuService.selectByBrand(dto);
    }
	@RequestMapping(value = "/select-by-brand-conut")
    public int count(@RequestBody HubSupplierSpuQureyDto dto){
    	return hubSpuService.count(dto);
    }
	@RequestMapping(value = "/select-by-primary-key/{spuId}")
    public HubSpu selectByPrimaryKey(@PathVariable(value = "spuId") Long spuId){
    	return hubSpuService.selectByPrimaryKey(spuId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSpuWithCriteria hubSpuWithCriteria){
    	return hubSpuService.updateByCriteriaSelective(hubSpuWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSpuWithCriteria hubSpuWithCriteria){
    	return hubSpuService.updateByCriteria(hubSpuWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSpu hubSpu){
    	return hubSpuService.updateByPrimaryKeySelective(hubSpu);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSpu hubSpu){
    	return hubSpuService.updateByPrimaryKey(hubSpu);
    }

	@RequestMapping(value = "/get-spuno")
	public String createSpuNo(){
		return   hubSpuUtil.createHubSpuNo(0L);
	}
}
