package com.shangpin.ephub.data.mysql.dictionary.material.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.dictionary.material.bean.HubMaterialDicItemCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.dictionary.material.bean.HubMaterialDicItemWithCriteria;
import com.shangpin.ephub.data.mysql.dictionary.material.po.HubMaterialDicItem;
import com.shangpin.ephub.data.mysql.dictionary.material.po.HubMaterialDicItemCriteria;
import com.shangpin.ephub.data.mysql.dictionary.material.service.HubMaterialDicItemService;

/**
 * 
 * <p>Title:HubMaterialDicItemController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:33:00
 */
@RestController
@RequestMapping("/hub-material-dic-item")
public class HubMaterialDicItemController {

	@Autowired
	private HubMaterialDicItemService hubMaterialDicItemService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubMaterialDicItemCriteria criteria){
    	return hubMaterialDicItemService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubMaterialDicItemCriteria criteria){
    	return hubMaterialDicItemService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long materialDicItemId){
    	return hubMaterialDicItemService.deleteByPrimaryKey(materialDicItemId);
    }
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubMaterialDicItem hubMaterialDicItem){
    	return hubMaterialDicItemService.insert(hubMaterialDicItem);
    }
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubMaterialDicItem hubMaterialDicItem){
    	return hubMaterialDicItemService.insertSelective(hubMaterialDicItem);
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubMaterialDicItem> selectByCriteriaWithRowbounds(@RequestBody HubMaterialDicItemCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubMaterialDicItemService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubMaterialDicItem> selectByCriteria(@RequestBody HubMaterialDicItemCriteria criteria){
    	return hubMaterialDicItemService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubMaterialDicItem selectByPrimaryKey(Long materialDicItemId){
    	return hubMaterialDicItemService.selectByPrimaryKey(materialDicItemId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubMaterialDicItemWithCriteria hubMaterialDicItemWithCriteria){
    	return hubMaterialDicItemService.updateByCriteriaSelective(hubMaterialDicItemWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubMaterialDicItemWithCriteria hubMaterialDicItemWithCriteria){
    	return hubMaterialDicItemService.updateByCriteria(hubMaterialDicItemWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubMaterialDicItem hubMaterialDicItem){
    	return hubMaterialDicItemService.updateByPrimaryKeySelective(hubMaterialDicItem);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubMaterialDicItem hubMaterialDicItem){
    	return hubMaterialDicItemService.updateByPrimaryKey(hubMaterialDicItem);
    }
}
