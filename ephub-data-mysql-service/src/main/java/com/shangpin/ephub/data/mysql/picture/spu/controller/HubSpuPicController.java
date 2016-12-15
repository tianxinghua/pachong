package com.shangpin.ephub.data.mysql.picture.spu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.picture.spu.bean.HubSpuPicCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.picture.spu.bean.HubSpuPicWithCriteria;
import com.shangpin.ephub.data.mysql.picture.spu.po.HubSpuPic;
import com.shangpin.ephub.data.mysql.picture.spu.po.HubSpuPicCriteria;
import com.shangpin.ephub.data.mysql.picture.spu.servie.HubSpuPicService;

/**
 * <p>Title:HubSpuPicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:55:19
 */
@RestController
@RequestMapping("/hub-spu-pic")
public class HubSpuPicController {

	@Autowired
	private HubSpuPicService hubSpuPicService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSpuPicCriteria criteria){
    	return hubSpuPicService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSpuPicCriteria criteria){
    	return hubSpuPicService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long spuPicId){
    	return hubSpuPicService.deleteByPrimaryKey(spuPicId);
    }
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSpuPic hubSpuPic){
    	return hubSpuPicService.insert(hubSpuPic);
    }
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSpuPic hubSpuPic){
    	return hubSpuPicService.insertSelective(hubSpuPic);
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSpuPic> selectByCriteriaWithRowbounds(@RequestBody HubSpuPicCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubSpuPicService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSpuPic> selectByCriteria(@RequestBody HubSpuPicCriteria criteria){
    	return hubSpuPicService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubSpuPic selectByPrimaryKey(Long spuPicId){
    	return hubSpuPicService.selectByPrimaryKey(spuPicId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSpuPicWithCriteria hubSpuPicWithCriteria){
    	return hubSpuPicService.updateByCriteriaSelective(hubSpuPicWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSpuPicWithCriteria hubSpuPicWithCriteria){
    	return hubSpuPicService.updateByCriteria(hubSpuPicWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSpuPic hubSpuPic){
    	return hubSpuPicService.updateByPrimaryKeySelective(hubSpuPic);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSpuPic hubSpuPic){
    	return hubSpuPicService.updateByPrimaryKey(hubSpuPic);
    }
}
