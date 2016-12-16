package com.shangpin.ephub.client.data.mysql.config.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shangpin.ephub.client.data.mysql.config.dto.HubSpuImportTaskCriteriaDto;
import com.shangpin.ephub.client.data.mysql.config.dto.HubSpuImportTaskCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.config.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.client.data.mysql.config.dto.HubSpuImportTaskWithCriteriaDto;

/**
 * <p>Title:HubSpuImportTaskController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:25:30
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSpuImportTaskGateWay {

	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSpuImportTaskCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSpuImportTaskCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long spuImportTaskId);
	
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSpuImportTaskDto hubSpuImportTask);
	
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSpuImportTaskDto hubSpuImportTask);
	
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSpuImportTaskDto> selectByCriteriaWithRowbounds(@RequestBody HubSpuImportTaskCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSpuImportTaskDto> selectByCriteria(@RequestBody HubSpuImportTaskCriteriaDto criteria);
	
	@RequestMapping(value = "/select-by-primary-key")
    public HubSpuImportTaskDto selectByPrimaryKey(Long spuImportTaskId);
	
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSpuImportTaskWithCriteriaDto hubSpuImportTaskWithCriteria);
	
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSpuImportTaskWithCriteriaDto hubSpuImportTaskWithCriteria);
	
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSpuImportTaskDto hubBrandDic);
	
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSpuImportTaskDto hubBrandDic);
}
