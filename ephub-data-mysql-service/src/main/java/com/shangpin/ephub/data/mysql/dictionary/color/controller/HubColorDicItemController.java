package com.shangpin.ephub.data.mysql.dictionary.color.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.dictionary.color.bean.HubColorDicItemCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.dictionary.color.bean.HubColorDicItemWithCriteria;
import com.shangpin.ephub.data.mysql.dictionary.color.po.HubColorDicItem;
import com.shangpin.ephub.data.mysql.dictionary.color.po.HubColorDicItemCriteria;
import com.shangpin.ephub.data.mysql.dictionary.color.service.HubColorDicItemService;
/**
 * <p>Title:HubColorDicItemController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:28:19
 */
@RestController
@RequestMapping("/hub-color-dic-item")
public class HubColorDicItemController {

	@Autowired
	private HubColorDicItemService hubColorDicItemService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubColorDicItemCriteria criteria){
    	return hubColorDicItemService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubColorDicItemCriteria criteria){
    	return hubColorDicItemService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long colorDicItemId){
    	return hubColorDicItemService.deleteByPrimaryKey(colorDicItemId);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody HubColorDicItem hubColorDicItem){
    	hubColorDicItemService.insert(hubColorDicItem);
    	return hubColorDicItem.getColorDicItemId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody HubColorDicItem hubColorDicItem){
    	hubColorDicItemService.insertSelective(hubColorDicItem);
    	return hubColorDicItem.getColorDicItemId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubColorDicItem> selectByCriteriaWithRowbounds(@RequestBody HubColorDicItemCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubColorDicItemService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubColorDicItem> selectByCriteria(@RequestBody HubColorDicItemCriteria criteria){
    	return hubColorDicItemService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubColorDicItem selectByPrimaryKey(Long colorDicItemId){
    	return hubColorDicItemService.selectByPrimaryKey(colorDicItemId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubColorDicItemWithCriteria hubColorDicItemWithCriteria){
    	return hubColorDicItemService.updateByCriteriaSelective(hubColorDicItemWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubColorDicItemWithCriteria hubColorDicItemWithCriteria){
    	return hubColorDicItemService.updateByCriteria(hubColorDicItemWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubColorDicItem hubColorDicItem){
    	return hubColorDicItemService.updateByPrimaryKeySelective(hubColorDicItem);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubColorDicItem hubColorDicItem){
    	return hubColorDicItemService.updateByPrimaryKey(hubColorDicItem);
    }
}
