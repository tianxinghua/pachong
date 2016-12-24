package com.shangpin.ephub.data.mysql.task.spuimport.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.task.spuimport.bean.HubSpuImportTaskCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.task.spuimport.bean.HubSpuImportTaskWithCriteria;
import com.shangpin.ephub.data.mysql.task.spuimport.po.HubSpuImportTask;
import com.shangpin.ephub.data.mysql.task.spuimport.po.HubSpuImportTaskCriteria;
import com.shangpin.ephub.data.mysql.task.spuimport.service.HubSpuImportTaskService;

/**
 * <p>Title:HubSpuImportTaskController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:25:30
 */
@RestController
@RequestMapping("/hub-spu-import-task")
public class HubSpuImportTaskController {

	@Autowired
	private HubSpuImportTaskService hubSpuImportTaskService;
	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSpuImportTaskCriteria criteria){
    	return hubSpuImportTaskService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSpuImportTaskCriteria criteria){
    	return hubSpuImportTaskService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long spuImportTaskId){
    	return hubSpuImportTaskService.deleteByPrimaryKey(spuImportTaskId);
    }
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody HubSpuImportTask hubSpuImportTask){
    	hubSpuImportTaskService.insert(hubSpuImportTask);
    	return hubSpuImportTask.getSpuImportTaskId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody HubSpuImportTask hubSpuImportTask){
    	hubSpuImportTaskService.insertSelective(hubSpuImportTask);
    	return hubSpuImportTask.getSpuImportTaskId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSpuImportTask> selectByCriteriaWithRowbounds(@RequestBody HubSpuImportTaskCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubSpuImportTaskService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSpuImportTask> selectByCriteria(@RequestBody HubSpuImportTaskCriteria criteria){
    	return hubSpuImportTaskService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubSpuImportTask selectByPrimaryKey(Long spuImportTaskId){
    	return hubSpuImportTaskService.selectByPrimaryKey(spuImportTaskId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSpuImportTaskWithCriteria hubSpuImportTaskWithCriteria){
    	return hubSpuImportTaskService.updateByCriteriaSelective(hubSpuImportTaskWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSpuImportTaskWithCriteria hubSpuImportTaskWithCriteria){
    	return hubSpuImportTaskService.updateByCriteria(hubSpuImportTaskWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSpuImportTask hubBrandDic){
    	return hubSpuImportTaskService.updateByPrimaryKeySelective(hubBrandDic);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSpuImportTask hubBrandDic){
    	return hubSpuImportTaskService.updateByPrimaryKey(hubBrandDic);
    }
}
