package com.shangpin.ephub.client.data.mysql.task.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskCriteriaDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskWithCriteriaDto;

/**
 * <p>Title:HubSpuImportTaskController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:25:30
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSpuImportTaskGateWay {

	
	@RequestMapping(value = "/hub-spu-import-task/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubSpuImportTaskCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-spu-import-task/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubSpuImportTaskCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-spu-import-task/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long spuImportTaskId);
	
	@RequestMapping(value = "/hub-spu-import-task/insert", method = RequestMethod.POST,consumes = "application/json")
    public int insert(@RequestBody HubSpuImportTaskDto hubSpuImportTask);
	
	@RequestMapping(value = "/hub-spu-import-task/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public int insertSelective(@RequestBody HubSpuImportTaskDto hubSpuImportTask);
	
	@RequestMapping(value = "/hub-spu-import-task/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSpuImportTaskDto> selectByCriteriaWithRowbounds(@RequestBody HubSpuImportTaskCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-spu-import-task/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSpuImportTaskDto> selectByCriteria(@RequestBody HubSpuImportTaskCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-spu-import-task/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubSpuImportTaskDto selectByPrimaryKey(Long spuImportTaskId);
	
	@RequestMapping(value = "/hub-spu-import-task/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubSpuImportTaskWithCriteriaDto hubSpuImportTaskWithCriteria);
	
	@RequestMapping(value = "/hub-spu-import-task/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubSpuImportTaskWithCriteriaDto hubSpuImportTaskWithCriteria);
	
	@RequestMapping(value = "/hub-spu-import-task/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubSpuImportTaskDto hubBrandDic);
	
	@RequestMapping(value = "/hub-spu-import-task/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubSpuImportTaskDto hubBrandDic);
}
