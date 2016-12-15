package com.shangpin.ephub.data.mysql.dictionary.season.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.dictionary.season.bean.HubSeasonDicCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.dictionary.season.bean.HubSeasonDicWithCriteria;
import com.shangpin.ephub.data.mysql.dictionary.season.po.HubSeasonDic;
import com.shangpin.ephub.data.mysql.dictionary.season.po.HubSeasonDicCriteria;
import com.shangpin.ephub.data.mysql.dictionary.season.service.HubSeasonDicService;
/**
 * <p>Title:HubSeasonDicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:37:10
 */
@RestController
@RequestMapping("/hub-season-dic")
public class HubSeasonDicController {

	@Autowired
	private HubSeasonDicService hubSeasonDicService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSeasonDicCriteria criteria){
    	return hubSeasonDicService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSeasonDicCriteria criteria){
    	return hubSeasonDicService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long seasonDicId){
    	return hubSeasonDicService.deleteByPrimaryKey(seasonDicId);
    }
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSeasonDic hubSeasonDic){
    	return hubSeasonDicService.insert(hubSeasonDic);
    }
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSeasonDic hubSeasonDic){
    	return hubSeasonDicService.insertSelective(hubSeasonDic);
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSeasonDic> selectByCriteriaWithRowbounds(@RequestBody HubSeasonDicCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubSeasonDicService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSeasonDic> selectByCriteria(@RequestBody HubSeasonDicCriteria criteria){
    	return hubSeasonDicService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubSeasonDic selectByPrimaryKey(Long seasonDicId){
    	return hubSeasonDicService.selectByPrimaryKey(seasonDicId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSeasonDicWithCriteria hubSeasonDicWithCriteria){
    	return hubSeasonDicService.updateByCriteriaSelective(hubSeasonDicWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSeasonDicWithCriteria hubSeasonDicWithCriteria){
    	return hubSeasonDicService.updateByCriteria(hubSeasonDicWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSeasonDic hubSeasonDic){
    	return hubSeasonDicService.updateByPrimaryKeySelective(hubSeasonDic);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSeasonDic hubSeasonDic){
    	return hubSeasonDicService.updateByPrimaryKey(hubSeasonDic);
    }
}
