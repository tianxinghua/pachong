package com.shangpin.ephub.data.mysql.picture.deleted.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.picture.deleted.bean.HubSpuPendingPicDeletedCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.picture.deleted.bean.HubSpuPendingPicDeletedWithCriteria;
import com.shangpin.ephub.data.mysql.picture.deleted.po.HubSpuPendingPicDeleted;
import com.shangpin.ephub.data.mysql.picture.deleted.po.HubSpuPendingPicDeletedCriteria;
import com.shangpin.ephub.data.mysql.picture.deleted.service.HubSpuPendingPicDeletedService;
/**
 * <p>Title:HubSpuPendingPicDeletedController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:53:21
 */
@RestController
@RequestMapping("/hub-spu-pending-pic-deleted")
public class HubSpuPendingPicDeletedController {

	@Autowired
	private HubSpuPendingPicDeletedService hubSpuPendingPicDeletedService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSpuPendingPicDeletedCriteria criteria){
    	return hubSpuPendingPicDeletedService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSpuPendingPicDeletedCriteria criteria){
    	return hubSpuPendingPicDeletedService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key/{spuPicId}")
    public int deleteByPrimaryKey(@PathVariable("spuPicId") Long spuPicId){
    	return hubSpuPendingPicDeletedService.deleteByPrimaryKey(spuPicId);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody HubSpuPendingPicDeleted hubSpuPendingPicDeleted){
    	hubSpuPendingPicDeletedService.insert(hubSpuPendingPicDeleted);
    	return hubSpuPendingPicDeleted.getSpuPendingPicId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody HubSpuPendingPicDeleted hubSpuPendingPicDeleted){
    	hubSpuPendingPicDeletedService.insertSelective(hubSpuPendingPicDeleted);
    	return hubSpuPendingPicDeleted.getSpuPendingId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSpuPendingPicDeleted> selectByCriteriaWithRowbounds(@RequestBody HubSpuPendingPicDeletedCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubSpuPendingPicDeletedService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSpuPendingPicDeleted> selectByCriteria(@RequestBody HubSpuPendingPicDeletedCriteria criteria){
    	return hubSpuPendingPicDeletedService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key/{spuPicId}")
    public HubSpuPendingPicDeleted selectByPrimaryKey(@PathVariable("spuPicId") Long spuPicId){
    	return hubSpuPendingPicDeletedService.selectByPrimaryKey(spuPicId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSpuPendingPicDeletedWithCriteria hubSpuPendingPicDeletedWithCriteria){
    	return hubSpuPendingPicDeletedService.updateByCriteriaSelective(hubSpuPendingPicDeletedWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSpuPendingPicDeletedWithCriteria hubSpuPendingPicDeletedWithCriteria){
    	return hubSpuPendingPicDeletedService.updateByCriteria(hubSpuPendingPicDeletedWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSpuPendingPicDeleted hubSpuPendingPicDeleted){
    	return hubSpuPendingPicDeletedService.updateByPrimaryKeySelective(hubSpuPendingPicDeleted);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSpuPendingPicDeleted hubSpuPendingPicDeleted){
    	return hubSpuPendingPicDeletedService.updateByPrimaryKey(hubSpuPendingPicDeleted);
    }
}
