package com.shangpin.ephub.data.mysql.spu.pendingnohand.controller;




import com.shangpin.ephub.data.mysql.spu.pendingnohand.bean.HubSpuPendingNohandleReasonCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.spu.pendingnohand.bean.HubSpuPendingNohandleReasonWithCriteria;
import com.shangpin.ephub.data.mysql.spu.pendingnohand.po.HubSpuPendingNohandleReason;
import com.shangpin.ephub.data.mysql.spu.pendingnohand.po.HubSpuPendingNohandleReasonCriteria;
import com.shangpin.ephub.data.mysql.spu.pendingnohand.service.HubSpuPendingNohandleReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *

 */
@RestController
@RequestMapping("/hub-spu-pending-nohandle")
public class HubSpuPendingNohandleReasonController {

	@Autowired
	private HubSpuPendingNohandleReasonService service;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSpuPendingNohandleReasonCriteria criteria){
    	return service.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSpuPendingNohandleReasonCriteria criteria){
    	return service.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long id){
    	return service.deleteByPrimaryKey(id);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody HubSpuPendingNohandleReason obj){
    	service.insert(obj);
    	return obj.getSpuPendingNohandleReasonId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody HubSpuPendingNohandleReason obj){
    	service.insertSelective(obj);
		return obj.getSpuPendingNohandleReasonId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSpuPendingNohandleReason> selectByCriteriaWithRowbounds(@RequestBody HubSpuPendingNohandleReasonCriteriaWithRowBounds criteriaWithRowBounds){
    	return service.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSpuPendingNohandleReason> selectByCriteria(@RequestBody HubSpuPendingNohandleReasonCriteria criteria){
    	return service.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubSpuPendingNohandleReason selectByPrimaryKey(Long id){
    	return service.selectByPrimaryKey(id);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSpuPendingNohandleReasonWithCriteria withCriteria){
    	return service.updateByCriteriaSelective(withCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSpuPendingNohandleReasonWithCriteria withCriteria){
    	return service.updateByCriteria(withCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSpuPendingNohandleReason obj){
    	return service.updateByPrimaryKeySelective(obj);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSpuPendingNohandleReason obj){
    	return service.updateByPrimaryKey(obj);
    }

	
}
