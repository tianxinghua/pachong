package com.shangpin.ephub.data.mysql.slot.spu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shangpin.ephub.data.mysql.slot.spu.bean.HubSlotSpuCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.slot.spu.bean.HubSlotSpuWithCriteria;
import com.shangpin.ephub.data.mysql.slot.spu.po.HubSlotSpu;
import com.shangpin.ephub.data.mysql.slot.spu.po.HubSlotSpuCriteria;
import com.shangpin.ephub.data.mysql.slot.spu.service.HubSlotSpuService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hub-slot-spu")
public class HubSlotSpuController {
	@Autowired
	private HubSlotSpuService HubSlotSpuService;

	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSlotSpuCriteria criteria){
    	return HubSlotSpuService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSlotSpuCriteria criteria){
    	return HubSlotSpuService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long id){
    	return HubSlotSpuService.deleteByPrimaryKey(id);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody HubSlotSpu hubSlotSpu){

    	 HubSlotSpuService.insert(hubSlotSpu);
		 return  hubSlotSpu.getSlotSpuId();
    }
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSlotSpu HubSlotSpu){
    	return HubSlotSpuService.insertSelective(HubSlotSpu);
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSlotSpu> selectByCriteriaWithRowbounds(@RequestBody HubSlotSpuCriteriaWithRowBounds criteriaWithRowBounds){
    	return HubSlotSpuService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSlotSpu> selectByCriteria(@RequestBody HubSlotSpuCriteria criteria){
    	return HubSlotSpuService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key/{id}")
    public HubSlotSpu selectByPrimaryKey(@PathVariable(value = "id") Long id){
    	return HubSlotSpuService.selectByPrimaryKey(id);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSlotSpuWithCriteria hubSlotSpuWithCriteria){
    	return HubSlotSpuService.updateByCriteriaSelective(hubSlotSpuWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSlotSpuWithCriteria HubSlotSpuWithCriteria){
    	return HubSlotSpuService.updateByCriteria(HubSlotSpuWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSlotSpu HubSlotSpu){
    	return HubSlotSpuService.updateByPrimaryKeySelective(HubSlotSpu);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSlotSpu HubSlotSpu){
    	return HubSlotSpuService.updateByPrimaryKey(HubSlotSpu);
    }
}
