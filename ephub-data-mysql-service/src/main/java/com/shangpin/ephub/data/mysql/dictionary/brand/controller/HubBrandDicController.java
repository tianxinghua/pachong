package com.shangpin.ephub.data.mysql.dictionary.brand.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.dictionary.brand.bean.HubBrandDicCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.dictionary.brand.bean.HubBrandDicWithCriteria;
import com.shangpin.ephub.data.mysql.dictionary.brand.po.HubBrandDic;
import com.shangpin.ephub.data.mysql.dictionary.brand.po.HubBrandDicCriteria;
import com.shangpin.ephub.data.mysql.dictionary.brand.service.HubBrandDicService;

/**
 * <p>Title:HubBrandDicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月13日 下午3:37:23
 */
@RestController
@RequestMapping(value = "/hub-brand-dic")
public class HubBrandDicController {
	
	@Autowired
	private HubBrandDicService hubBrandDicService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubBrandDicCriteria criteria){
    	return hubBrandDicService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubBrandDicCriteria criteria){
    	return hubBrandDicService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long brandDicId){
    	return hubBrandDicService.deleteByPrimaryKey(brandDicId);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody HubBrandDic hubBrandDic){
    	hubBrandDicService.insert(hubBrandDic);
    	return hubBrandDic.getBrandDicId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody HubBrandDic hubBrandDic){
    	hubBrandDicService.insertSelective(hubBrandDic);
    	return hubBrandDic.getBrandDicId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubBrandDic> selectByCriteriaWithRowbounds(@RequestBody HubBrandDicCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubBrandDicService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubBrandDic> selectByCriteria(@RequestBody HubBrandDicCriteria criteria){
    	return hubBrandDicService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubBrandDic selectByPrimaryKey(Long brandDicId){
    	return hubBrandDicService.selectByPrimaryKey(brandDicId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubBrandDicWithCriteria hubBrandDicWithCriteria){
    	return hubBrandDicService.updateByCriteriaSelective(hubBrandDicWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubBrandDicWithCriteria hubBrandDicWithCriteria){
    	return hubBrandDicService.updateByCriteria(hubBrandDicWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubBrandDic hubBrandDic){
    	return hubBrandDicService.updateByPrimaryKeySelective(hubBrandDic);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubBrandDic hubBrandDic){
    	return hubBrandDicService.updateByPrimaryKey(hubBrandDic);
    }
}
