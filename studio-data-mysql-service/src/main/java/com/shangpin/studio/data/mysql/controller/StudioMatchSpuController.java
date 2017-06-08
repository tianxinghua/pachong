package com.shangpin.studio.data.mysql.controller;


import com.shangpin.studio.data.mysql.bean.StudioMatchSpuCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioMatchSpuWithCriteria;
import com.shangpin.studio.data.mysql.po.StudioMatchSpu;
import com.shangpin.studio.data.mysql.po.StudioMatchSpuCriteria;
import com.shangpin.studio.data.mysql.service.StudioMatchSpuService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *

 */
@RestController
@RequestMapping("/studio-match-spu")
public class StudioMatchSpuController {

	@Autowired
	private StudioMatchSpuService service;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody StudioMatchSpuCriteria criteria){
    	return service.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody StudioMatchSpuCriteria criteria){
    	return service.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long id){
    	return service.deleteByPrimaryKey(id);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody StudioMatchSpu obj){
    	service.insert(obj);
    	return obj.getStudioMatchSpuId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody StudioMatchSpu obj){
    	service.insertSelective(obj);
		return obj.getStudioMatchSpuId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<StudioMatchSpu> selectByCriteriaWithRowbounds(@RequestBody StudioMatchSpuCriteriaWithRowBounds criteriaWithRowBounds){
    	return service.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<StudioMatchSpu> selectByCriteria(@RequestBody StudioMatchSpuCriteria criteria){
    	return service.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public StudioMatchSpu selectByPrimaryKey(Long id){
    	return service.selectByPrimaryKey(id);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody StudioMatchSpuWithCriteria withCriteria){
    	return service.updateByCriteriaSelective(withCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody StudioMatchSpuWithCriteria withCriteria){
    	return service.updateByCriteria(withCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody StudioMatchSpu obj){
    	return service.updateByPrimaryKeySelective(obj);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody StudioMatchSpu obj){
    	return service.updateByPrimaryKey(obj);
    }

	
}
