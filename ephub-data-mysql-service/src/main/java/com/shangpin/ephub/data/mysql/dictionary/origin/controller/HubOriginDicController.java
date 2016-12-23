package com.shangpin.ephub.data.mysql.dictionary.origin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.dictionary.origin.bean.HubOriginDicCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.dictionary.origin.bean.HubOriginDicWithCriteria;
import com.shangpin.ephub.data.mysql.dictionary.origin.po.HubOriginDic;
import com.shangpin.ephub.data.mysql.dictionary.origin.po.HubOriginDicCriteria;
import com.shangpin.ephub.data.mysql.dictionary.origin.service.HubOriginDicService;

/**
 * <p>Title:HubOriginDicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月22日 下午12:25:04
 */
@RestController
@RequestMapping("/hub-origin-dic")
public class HubOriginDicController {

	@Autowired
	private HubOriginDicService hubOriginDicService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubOriginDicCriteria criteria){
    	return hubOriginDicService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubOriginDicCriteria criteria){
    	return hubOriginDicService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long originDicId){
    	return hubOriginDicService.deleteByPrimaryKey(originDicId);
    }
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubOriginDic hubOriginDic){
    	return hubOriginDicService.insert(hubOriginDic);
    }
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubOriginDic hubOriginDic){
    	return hubOriginDicService.insertSelective(hubOriginDic);
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubOriginDic> selectByCriteriaWithRowbounds(@RequestBody HubOriginDicCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubOriginDicService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubOriginDic> selectByCriteria(@RequestBody HubOriginDicCriteria criteria){
    	return hubOriginDicService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubOriginDic selectByPrimaryKey(Long originDicId){
    	return hubOriginDicService.selectByPrimaryKey(originDicId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubOriginDicWithCriteria hubOriginDicWithCriteria){
    	return hubOriginDicService.updateByCriteriaSelective(hubOriginDicWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubOriginDicWithCriteria hubOriginDicWithCriteria){
    	return hubOriginDicService.updateByCriteria(hubOriginDicWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubOriginDic hubOriginDic){
    	return hubOriginDicService.updateByPrimaryKeySelective(hubOriginDic);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubOriginDic hubOriginDic){
    	return hubOriginDicService.updateByPrimaryKey(hubOriginDic);
    }
}
