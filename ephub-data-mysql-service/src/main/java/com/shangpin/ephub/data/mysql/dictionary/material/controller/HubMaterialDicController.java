package com.shangpin.ephub.data.mysql.dictionary.material.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.dictionary.material.bean.HubMaterialDicCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.dictionary.material.bean.HubMaterialDicWithCriteria;
import com.shangpin.ephub.data.mysql.dictionary.material.po.HubMaterialDic;
import com.shangpin.ephub.data.mysql.dictionary.material.po.HubMaterialDicCriteria;
import com.shangpin.ephub.data.mysql.dictionary.material.service.HubMaterialDicService;

/**
 * <p>Title:HubMaterialDicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月15日 下午3:02:13
 */
@RestController
@RequestMapping("/hub-material-dic")
public class HubMaterialDicController {

	@Autowired
	private HubMaterialDicService hubMaterialDicService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubMaterialDicCriteria criteria){
    	return hubMaterialDicService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubMaterialDicCriteria criteria){
    	return hubMaterialDicService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long materialDicId){
    	return hubMaterialDicService.deleteByPrimaryKey(materialDicId);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody HubMaterialDic hubMaterialDic){
    	hubMaterialDicService.insert(hubMaterialDic);
    	return hubMaterialDic.getMaterialDicId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody HubMaterialDic hubMaterialDic){
    	hubMaterialDicService.insertSelective(hubMaterialDic);
    	return hubMaterialDic.getMaterialDicId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubMaterialDic> selectByCriteriaWithRowbounds(@RequestBody HubMaterialDicCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubMaterialDicService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubMaterialDic> selectByCriteria(@RequestBody HubMaterialDicCriteria criteria){
    	return hubMaterialDicService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubMaterialDic selectByPrimaryKey(Long materialDicId){
    	return hubMaterialDicService.selectByPrimaryKey(materialDicId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubMaterialDicWithCriteria hubMaterialDicWithCriteria){
    	return hubMaterialDicService.updateByCriteriaSelective(hubMaterialDicWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubMaterialDicWithCriteria hubMaterialDicWithCriteria){
    	return hubMaterialDicService.updateByCriteria(hubMaterialDicWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubMaterialDic hubMaterialDic){
    	return hubMaterialDicService.updateByPrimaryKeySelective(hubMaterialDic);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubMaterialDic hubMaterialDic){
    	return hubMaterialDicService.updateByPrimaryKey(hubMaterialDic);
    }
}
