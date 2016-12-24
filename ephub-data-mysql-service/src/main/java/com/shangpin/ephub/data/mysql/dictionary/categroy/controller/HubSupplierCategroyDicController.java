package com.shangpin.ephub.data.mysql.dictionary.categroy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.dictionary.categroy.bean.HubSupplierCategroyDicCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.dictionary.categroy.bean.HubSupplierCategroyDicWithCriteria;
import com.shangpin.ephub.data.mysql.dictionary.categroy.po.HubSupplierCategroyDic;
import com.shangpin.ephub.data.mysql.dictionary.categroy.po.HubSupplierCategroyDicCriteria;
import com.shangpin.ephub.data.mysql.dictionary.categroy.service.HubSupplierCategroyDicService;

/**
 * <p>Title:HubSupplierCategroyDicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:19:55
 */
@RestController
@RequestMapping("/hub-supplier-categroy-dic")
public class HubSupplierCategroyDicController {

	@Autowired
	private HubSupplierCategroyDicService hubSupplierCategroyDicService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSupplierCategroyDicCriteria criteria){
    	return hubSupplierCategroyDicService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSupplierCategroyDicCriteria criteria){
    	return hubSupplierCategroyDicService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long supplierCategoryDicId){
    	return hubSupplierCategroyDicService.deleteByPrimaryKey(supplierCategoryDicId);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody HubSupplierCategroyDic hubSupplierCategroyDic){
    	hubSupplierCategroyDicService.insert(hubSupplierCategroyDic);
    	return hubSupplierCategroyDic.getSupplierCategoryDicId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody HubSupplierCategroyDic hubSupplierCategroyDic){
    	hubSupplierCategroyDicService.insertSelective(hubSupplierCategroyDic);
    	return hubSupplierCategroyDic.getSupplierCategoryDicId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSupplierCategroyDic> selectByCriteriaWithRowbounds(@RequestBody HubSupplierCategroyDicCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubSupplierCategroyDicService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSupplierCategroyDic> selectByCriteria(@RequestBody HubSupplierCategroyDicCriteria criteria){
    	return hubSupplierCategroyDicService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubSupplierCategroyDic selectByPrimaryKey(Long supplierCategoryDicId){
    	return hubSupplierCategroyDicService.selectByPrimaryKey(supplierCategoryDicId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSupplierCategroyDicWithCriteria hubSupplierCategroyDicWithCriteria){
    	return hubSupplierCategroyDicService.updateByCriteriaSelective(hubSupplierCategroyDicWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSupplierCategroyDicWithCriteria hubSupplierCategroyDicWithCriteria){
    	return hubSupplierCategroyDicService.updateByCriteria(hubSupplierCategroyDicWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierCategroyDic hubSupplierCategroyDic){
    	return hubSupplierCategroyDicService.updateByPrimaryKeySelective(hubSupplierCategroyDic);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSupplierCategroyDic hubSupplierCategroyDic){
    	return hubSupplierCategroyDicService.updateByPrimaryKey(hubSupplierCategroyDic);
    }
}
