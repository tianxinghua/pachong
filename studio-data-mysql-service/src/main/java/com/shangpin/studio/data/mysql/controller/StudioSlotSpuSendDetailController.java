package com.shangpin.studio.data.mysql.controller;


import com.shangpin.studio.data.mysql.bean.StudioSlotSpuSendDetailCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioSlotSpuSendDetailWithCriteria;
import com.shangpin.studio.data.mysql.po.StudioSlotSpuSendDetail;
import com.shangpin.studio.data.mysql.po.StudioSlotSpuSendDetailCriteria;
import com.shangpin.studio.data.mysql.service.StudioSlotService;
import com.shangpin.studio.data.mysql.service.StudioSlotSpuSendDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *

 */
@RestController
@RequestMapping("/studio-slot-spu-send-detail")
public class StudioSlotSpuSendDetailController {

	@Autowired
	private StudioSlotSpuSendDetailService service;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody StudioSlotSpuSendDetailCriteria criteria){
    	return service.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody StudioSlotSpuSendDetailCriteria criteria){
    	return service.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long id){
    	return service.deleteByPrimaryKey(id);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody StudioSlotSpuSendDetail obj){
    	service.insert(obj);
    	return obj.getStudioSlotSpuSendDetailId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody StudioSlotSpuSendDetail obj){
    	service.insertSelective(obj);
		return obj.getStudioSlotSpuSendDetailId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<StudioSlotSpuSendDetail> selectByCriteriaWithRowbounds(@RequestBody StudioSlotSpuSendDetailCriteriaWithRowBounds criteriaWithRowBounds){
    	return service.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<StudioSlotSpuSendDetail> selectByCriteria(@RequestBody StudioSlotSpuSendDetailCriteria criteria){
    	return service.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public StudioSlotSpuSendDetail selectByPrimaryKey(Long id){
    	return service.selectByPrimaryKey(id);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody StudioSlotSpuSendDetailWithCriteria criteria){
    	return service.updateByCriteriaSelective(criteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody StudioSlotSpuSendDetailWithCriteria criteria){
    	return service.updateByCriteria(criteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody StudioSlotSpuSendDetail obj){
    	return service.updateByPrimaryKeySelective(obj);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody StudioSlotSpuSendDetail obj){
    	return service.updateByPrimaryKey(obj);
    }

	
}
