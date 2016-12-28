package com.shangpin.ephub.data.mysql.spu.supplier.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.spu.supplier.bean.HubSupplierSpuCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.spu.supplier.bean.HubSupplierSpuWithCriteria;
import com.shangpin.ephub.data.mysql.spu.supplier.po.HubSupplierSpu;
import com.shangpin.ephub.data.mysql.spu.supplier.po.HubSupplierSpuCriteria;
import com.shangpin.ephub.data.mysql.spu.supplier.servcie.HubSupplierSpuService;
/**
 * <p>Title:HubSupplierSpuController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:21:45
 */
@RestController
@RequestMapping("/hub-supplier-spu")
public class HubSupplierSpuController {

	@Autowired
	private HubSupplierSpuService hubSupplierSpuService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSupplierSpuCriteria criteria){
    	return hubSupplierSpuService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSupplierSpuCriteria criteria){
    	return hubSupplierSpuService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long supplierSpuId){
    	return hubSupplierSpuService.deleteByPrimaryKey(supplierSpuId);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody HubSupplierSpu hubSupplierSpu){
    	hubSupplierSpuService.insert(hubSupplierSpu);
    	return hubSupplierSpu.getSupplierSpuId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody HubSupplierSpu hubSupplierSpu){
    	hubSupplierSpuService.insertSelective(hubSupplierSpu);
    	return hubSupplierSpu.getSupplierSpuId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSupplierSpu> selectByCriteriaWithRowbounds(@RequestBody HubSupplierSpuCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubSupplierSpuService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSupplierSpu> selectByCriteria(@RequestBody HubSupplierSpuCriteria criteria){
    	return hubSupplierSpuService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubSupplierSpu selectByPrimaryKey(Long supplierSpuId){
    	return hubSupplierSpuService.selectByPrimaryKey(supplierSpuId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSupplierSpuWithCriteria hubSupplierSpuWithCriteria){
    	return hubSupplierSpuService.updateByCriteriaSelective(hubSupplierSpuWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSupplierSpuWithCriteria hubSupplierSpuWithCriteria){
    	return hubSupplierSpuService.updateByCriteria(hubSupplierSpuWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierSpu hubSupplierSpu){
    	return hubSupplierSpuService.updateByPrimaryKeySelective(hubSupplierSpu);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSupplierSpu hubSupplierSpu){
    	return hubSupplierSpuService.updateByPrimaryKey(hubSupplierSpu);
    }
}
