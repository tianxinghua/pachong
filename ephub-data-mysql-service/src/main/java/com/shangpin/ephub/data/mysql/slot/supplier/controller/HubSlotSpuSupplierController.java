package com.shangpin.ephub.data.mysql.slot.supplier.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.shangpin.ephub.data.mysql.slot.supplier.bean.HubSlotSpuSupplierCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.slot.supplier.bean.HubSlotSpuSupplierWithCriteria;
import com.shangpin.ephub.data.mysql.slot.supplier.po.HubSlotSpuSupplier;
import com.shangpin.ephub.data.mysql.slot.supplier.po.HubSlotSpuSupplierCriteria;
import com.shangpin.ephub.data.mysql.slot.supplier.service.HubSlotSpuSupplierService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hub-slot-spu-supplier")
public class HubSlotSpuSupplierController {
	@Autowired
	private HubSlotSpuSupplierService hubSlotSpuSupplierService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSlotSpuSupplierCriteria criteria){
    	return hubSlotSpuSupplierService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSlotSpuSupplierCriteria criteria){
    	return hubSlotSpuSupplierService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long skuId){
    	return hubSlotSpuSupplierService.deleteByPrimaryKey(skuId);
    }
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSlotSpuSupplier HubSlotSpuSupplier){
    	return hubSlotSpuSupplierService.insert(HubSlotSpuSupplier);
    }
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSlotSpuSupplier HubSlotSpuSupplier){
    	return hubSlotSpuSupplierService.insertSelective(HubSlotSpuSupplier);
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSlotSpuSupplier> selectByCriteriaWithRowbounds(@RequestBody HubSlotSpuSupplierCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubSlotSpuSupplierService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSlotSpuSupplier> selectByCriteria(@RequestBody HubSlotSpuSupplierCriteria criteria){
    	return hubSlotSpuSupplierService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key/{skuId}")
    public HubSlotSpuSupplier selectByPrimaryKey(@PathVariable(value = "skuId") Long skuId){
    	return hubSlotSpuSupplierService.selectByPrimaryKey(skuId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSlotSpuSupplierWithCriteria HubSlotSpuSupplierWithCriteria){
    	return hubSlotSpuSupplierService.updateByCriteriaSelective(HubSlotSpuSupplierWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSlotSpuSupplierWithCriteria HubSlotSpuSupplierWithCriteria){
    	return hubSlotSpuSupplierService.updateByCriteria(HubSlotSpuSupplierWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSlotSpuSupplier HubSlotSpuSupplier){
    	return hubSlotSpuSupplierService.updateByPrimaryKeySelective(HubSlotSpuSupplier);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSlotSpuSupplier HubSlotSpuSupplier){
    	return hubSlotSpuSupplierService.updateByPrimaryKey(HubSlotSpuSupplier);
    }
}
