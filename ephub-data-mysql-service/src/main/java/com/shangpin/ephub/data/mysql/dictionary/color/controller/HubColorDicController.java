package com.shangpin.ephub.data.mysql.dictionary.color.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.dictionary.color.bean.HubColorDicCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.dictionary.color.bean.HubColorDicWithCriteria;
import com.shangpin.ephub.data.mysql.dictionary.color.po.HubColorDic;
import com.shangpin.ephub.data.mysql.dictionary.color.po.HubColorDicCriteria;
import com.shangpin.ephub.data.mysql.dictionary.color.service.HubColorDicService;

/**
 * <p>Title:HubColorDicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:24:58
 */
@RestController
@RequestMapping("/hub-color-dic")
public class HubColorDicController {

	@Autowired
	private HubColorDicService hubColorDicService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubColorDicCriteria criteria){
    	return hubColorDicService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubColorDicCriteria criteria){
    	return hubColorDicService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long colorDicId){
    	return hubColorDicService.deleteByPrimaryKey(colorDicId);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody HubColorDic hubColorDic){
    	hubColorDicService.insert(hubColorDic);
    	return hubColorDic.getColorDicId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody HubColorDic hubColorDic){
    	hubColorDicService.insertSelective(hubColorDic);
    	return hubColorDic.getColorDicId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubColorDic> selectByCriteriaWithRowbounds(@RequestBody HubColorDicCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubColorDicService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubColorDic> selectByCriteria(@RequestBody HubColorDicCriteria criteria){
    	return hubColorDicService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubColorDic selectByPrimaryKey(Long colorDicId){
    	return hubColorDicService.selectByPrimaryKey(colorDicId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubColorDicWithCriteria hubColorDicWithCriteria){
    	return hubColorDicService.updateByCriteriaSelective(hubColorDicWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubColorDicWithCriteria hubColorDicWithCriteria){
    	return hubColorDicService.updateByCriteria(hubColorDicWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubColorDic hubColorDic){
    	return hubColorDicService.updateByPrimaryKeySelective(hubColorDic);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubColorDic hubColorDic){
    	return hubColorDicService.updateByPrimaryKey(hubColorDic);
    }
}
