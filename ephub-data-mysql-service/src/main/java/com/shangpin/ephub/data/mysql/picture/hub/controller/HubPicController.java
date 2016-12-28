package com.shangpin.ephub.data.mysql.picture.hub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.picture.hub.bean.HubPicCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.picture.hub.bean.HubPicWithCriteria;
import com.shangpin.ephub.data.mysql.picture.hub.po.HubPic;
import com.shangpin.ephub.data.mysql.picture.hub.po.HubPicCriteria;
import com.shangpin.ephub.data.mysql.picture.hub.service.HubPicService;

/**
 * <p>Title:HubPicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:50:53
 */
@RestController
@RequestMapping("/hub-pic")
public class HubPicController {

	@Autowired
	private HubPicService hubPicService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubPicCriteria criteria){
    	return hubPicService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubPicCriteria criteria){
    	return hubPicService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long picId){
    	return hubPicService.deleteByPrimaryKey(picId);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody HubPic hubPic){
    	hubPicService.insert(hubPic);
    	return hubPic.getPicId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody HubPic hubPic){
    	hubPicService.insertSelective(hubPic);
    	return hubPic.getPicId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubPic> selectByCriteriaWithRowbounds(@RequestBody HubPicCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubPicService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubPic> selectByCriteria(@RequestBody HubPicCriteria criteria){
    	return hubPicService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubPic selectByPrimaryKey(Long picId){
    	return hubPicService.selectByPrimaryKey(picId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubPicWithCriteria hubPicWithCriteria){
    	return hubPicService.updateByCriteriaSelective(hubPicWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubPicWithCriteria hubPicWithCriteria){
    	return hubPicService.updateByCriteria(hubPicWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubPic hubPic){
    	return hubPicService.updateByPrimaryKeySelective(hubPic);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubPic hubPic){
    	return hubPicService.updateByPrimaryKey(hubPic);
    }
}
