package com.shangpin.ephub.data.mysql.dictionary.brand.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.dictionary.brand.bean.HubSupplierBrandDicCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.dictionary.brand.bean.HubSupplierBrandDicWithCriteria;
import com.shangpin.ephub.data.mysql.dictionary.brand.po.HubSupplierBrandDic;
import com.shangpin.ephub.data.mysql.dictionary.brand.po.HubSupplierBrandDicCriteria;
import com.shangpin.ephub.data.mysql.dictionary.brand.service.HubSupplierBrandDicService;

/**
 * <p>Title:HubSupplierBrandDicController.java </p>
 * <p>Description: 供应商品牌字典</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月13日 下午3:37:59
 */
@RestController
@RequestMapping(value = "/hub-supplier-brand-dic")
public class HubSupplierBrandDicController {
	
	@Autowired
	private HubSupplierBrandDicService hubSupplierBrandDicService;

	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSupplierBrandDicCriteria criteria){
    	return hubSupplierBrandDicService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSupplierBrandDicCriteria criteria){
    	return hubSupplierBrandDicService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long supplierBrandDicId){
    	return hubSupplierBrandDicService.deleteByPrimaryKey(supplierBrandDicId);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody HubSupplierBrandDic hubSupplierBrandDic){
    	hubSupplierBrandDicService.insert(hubSupplierBrandDic);
    	return hubSupplierBrandDic.getSupplierBrandDicId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody HubSupplierBrandDic hubSupplierBrandDic){
    	hubSupplierBrandDicService.insertSelective(hubSupplierBrandDic);
    	return hubSupplierBrandDic.getSupplierBrandDicId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSupplierBrandDic> selectByCriteriaWithRowbounds(@RequestBody HubSupplierBrandDicCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubSupplierBrandDicService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSupplierBrandDic> selectByCriteria(@RequestBody HubSupplierBrandDicCriteria criteria){
    	return hubSupplierBrandDicService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubSupplierBrandDic selectByPrimaryKey(Long supplierBrandDicId){
    	return hubSupplierBrandDicService.selectByPrimaryKey(supplierBrandDicId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSupplierBrandDicWithCriteria hubSupplierBrandDicWithCriteria){
    	return hubSupplierBrandDicService.updateByCriteriaSelective(hubSupplierBrandDicWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSupplierBrandDicWithCriteria hubSupplierBrandDicWithCriteria){
    	return hubSupplierBrandDicService.updateByCriteria(hubSupplierBrandDicWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierBrandDic hubSupplierBrandDic){
    	return hubSupplierBrandDicService.updateByPrimaryKeySelective(hubSupplierBrandDic);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSupplierBrandDic hubSupplierBrandDic){
    	return hubSupplierBrandDicService.updateByPrimaryKey(hubSupplierBrandDic);
    }
}
