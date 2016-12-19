package com.shangpin.ephub.data.mysql.picture.pending.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.picture.pending.bean.HubSpuPendingPicCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.picture.pending.bean.HubSpuPendingPicWithCriteria;
import com.shangpin.ephub.data.mysql.picture.pending.po.HubSpuPendingPic;
import com.shangpin.ephub.data.mysql.picture.pending.po.HubSpuPendingPicCriteria;
import com.shangpin.ephub.data.mysql.picture.pending.service.HubSpuPendingPicService;
/**
 * <p>Title:HubSpuPendingPicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:53:21
 */
@RestController
@RequestMapping("/hub-spu-pending-pic")
public class HubSpuPendingPicController {

	@Autowired
	private HubSpuPendingPicService hubSpuPendingPicService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSpuPendingPicCriteria criteria){
    	return hubSpuPendingPicService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSpuPendingPicCriteria criteria){
    	return hubSpuPendingPicService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long spuPicId){
    	return hubSpuPendingPicService.deleteByPrimaryKey(spuPicId);
    }
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSpuPendingPic hubSpuPendingPic){
    	return hubSpuPendingPicService.insert(hubSpuPendingPic);
    }
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSpuPendingPic hubSpuPendingPic){
    	return hubSpuPendingPicService.insertSelective(hubSpuPendingPic);
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSpuPendingPic> selectByCriteriaWithRowbounds(@RequestBody HubSpuPendingPicCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubSpuPendingPicService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSpuPendingPic> selectByCriteria(@RequestBody HubSpuPendingPicCriteria criteria){
    	return hubSpuPendingPicService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubSpuPendingPic selectByPrimaryKey(Long spuPicId){
    	return hubSpuPendingPicService.selectByPrimaryKey(spuPicId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSpuPendingPicWithCriteria hubSpuPendingPicWithCriteria){
    	return hubSpuPendingPicService.updateByCriteriaSelective(hubSpuPendingPicWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSpuPendingPicWithCriteria hubSpuPendingPicWithCriteria){
    	return hubSpuPendingPicService.updateByCriteria(hubSpuPendingPicWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSpuPendingPic hubSpuPendingPic){
    	return hubSpuPendingPicService.updateByPrimaryKeySelective(hubSpuPendingPic);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSpuPendingPic hubSpuPendingPic){
    	return hubSpuPendingPicService.updateByPrimaryKey(hubSpuPendingPic);
    }
}