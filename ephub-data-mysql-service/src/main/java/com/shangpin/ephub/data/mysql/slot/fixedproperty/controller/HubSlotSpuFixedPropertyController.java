package com.shangpin.ephub.data.mysql.slot.fixedproperty.controller;




import com.shangpin.ephub.data.mysql.slot.fixedproperty.bean.HubSlotSpuFixedPropertyCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.slot.fixedproperty.bean.HubSlotSpuFixedPropertyWithCriteria;
import com.shangpin.ephub.data.mysql.slot.fixedproperty.po.HubSlotSpuFixedProperty;
import com.shangpin.ephub.data.mysql.slot.fixedproperty.po.HubSlotSpuFixedPropertyCriteria;
import com.shangpin.ephub.data.mysql.slot.fixedproperty.service.HubSlotSpuFixedPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *

 */
@RestController
@RequestMapping("/studio-slot-fixed-property")
public class HubSlotSpuFixedPropertyController {

	@Autowired
	private HubSlotSpuFixedPropertyService service;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSlotSpuFixedPropertyCriteria criteria){
    	return service.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSlotSpuFixedPropertyCriteria criteria){
    	return service.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long id){
    	return service.deleteByPrimaryKey(id);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody HubSlotSpuFixedProperty obj){
    	service.insert(obj);
    	return obj.getId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody HubSlotSpuFixedProperty obj){
    	service.insertSelective(obj);
		return obj.getId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSlotSpuFixedProperty> selectByCriteriaWithRowbounds(@RequestBody HubSlotSpuFixedPropertyCriteriaWithRowBounds criteriaWithRowBounds){
    	return service.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSlotSpuFixedProperty> selectByCriteria(@RequestBody HubSlotSpuFixedPropertyCriteria criteria){
    	return service.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubSlotSpuFixedProperty selectByPrimaryKey(Long id){
    	return service.selectByPrimaryKey(id);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSlotSpuFixedPropertyWithCriteria withCriteria){
    	return service.updateByCriteriaSelective(withCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSlotSpuFixedPropertyWithCriteria withCriteria){
    	return service.updateByCriteria(withCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSlotSpuFixedProperty obj){
    	return service.updateByPrimaryKeySelective(obj);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSlotSpuFixedProperty obj){
    	return service.updateByPrimaryKey(obj);
    }

	
}
