package com.shangpin.ephub.data.mysql.dictionary.gender.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.dictionary.gender.bean.HubGenderDicCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.dictionary.gender.bean.HubGenderDicWithCriteria;
import com.shangpin.ephub.data.mysql.dictionary.gender.po.HubGenderDic;
import com.shangpin.ephub.data.mysql.dictionary.gender.po.HubGenderDicCriteria;
import com.shangpin.ephub.data.mysql.dictionary.gender.service.HubGenderDicService;
/**
 * <p>Title:HubGenderDicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:30:46
 */
@RestController
@RequestMapping("/hub-gender-dic")
public class HubGenderDicController {

	@Autowired
	private HubGenderDicService hubGenderDicService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubGenderDicCriteria criteria){
    	return hubGenderDicService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubGenderDicCriteria criteria){
    	return hubGenderDicService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long genderDicId){
    	return hubGenderDicService.deleteByPrimaryKey(genderDicId);
    }
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubGenderDic hubGenderDic){
    	return hubGenderDicService.insert(hubGenderDic);
    }
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubGenderDic hubGenderDic){
    	return hubGenderDicService.insertSelective(hubGenderDic);
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubGenderDic> selectByCriteriaWithRowbounds(@RequestBody HubGenderDicCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubGenderDicService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubGenderDic> selectByCriteria(@RequestBody HubGenderDicCriteria criteria){
    	return hubGenderDicService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubGenderDic selectByPrimaryKey(Long genderDicId){
    	return hubGenderDicService.selectByPrimaryKey(genderDicId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubGenderDicWithCriteria hubGenderDicWithCriteria){
    	return hubGenderDicService.updateByCriteriaSelective(hubGenderDicWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubGenderDicWithCriteria hubGenderDicWithCriteria){
    	return hubGenderDicService.updateByCriteria(hubGenderDicWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubGenderDic hubGenderDic){
    	return hubGenderDicService.updateByPrimaryKeySelective(hubGenderDic);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubGenderDic hubGenderDic){
    	return hubGenderDicService.updateByPrimaryKey(hubGenderDic);
    }
}
