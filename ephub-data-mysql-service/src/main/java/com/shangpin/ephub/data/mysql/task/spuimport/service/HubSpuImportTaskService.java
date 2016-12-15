package com.shangpin.ephub.data.mysql.task.spuimport.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.task.spuimport.bean.HubSpuImportTaskCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.task.spuimport.bean.HubSpuImportTaskWithCriteria;
import com.shangpin.ephub.data.mysql.task.spuimport.mapper.HubSpuImportTaskMapper;
import com.shangpin.ephub.data.mysql.task.spuimport.po.HubSpuImportTask;
import com.shangpin.ephub.data.mysql.task.spuimport.po.HubSpuImportTaskCriteria;

/**
 * <p>Title:HubSpuImportTaskService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:25:06
 */
@Service
public class HubSpuImportTaskService {

	@Autowired
	private HubSpuImportTaskMapper hubSpuImportTaskMapper;

	public int countByCriteria(HubSpuImportTaskCriteria criteria) {
		return hubSpuImportTaskMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSpuImportTaskCriteria criteria) {
		return hubSpuImportTaskMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long spuImportTaskId) {
		return hubSpuImportTaskMapper.deleteByPrimaryKey(spuImportTaskId);
	}

	public int insert(HubSpuImportTask hubSpuImportTask) {
		return hubSpuImportTaskMapper.insert(hubSpuImportTask);
	}

	public int insertSelective(HubSpuImportTask hubSpuImportTask) {
		return hubSpuImportTaskMapper.insertSelective(hubSpuImportTask);
	}

	public List<HubSpuImportTask> selectByCriteriaWithRowbounds(
			HubSpuImportTaskCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubSpuImportTaskMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubSpuImportTask> selectByCriteria(HubSpuImportTaskCriteria criteria) {
		return hubSpuImportTaskMapper.selectByExample(criteria);
	}

	public HubSpuImportTask selectByPrimaryKey(Long spuImportTaskId) {
		return hubSpuImportTaskMapper.selectByPrimaryKey(spuImportTaskId);
	}

	public int updateByCriteriaSelective(HubSpuImportTaskWithCriteria hubSpuImportTaskWithCriteria) {
		return hubSpuImportTaskMapper.updateByExampleSelective(hubSpuImportTaskWithCriteria.getHubSpuImportTask(), hubSpuImportTaskWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubSpuImportTaskWithCriteria hubSpuImportTaskWithCriteria) {
		return hubSpuImportTaskMapper.updateByExample(hubSpuImportTaskWithCriteria.getHubSpuImportTask(), hubSpuImportTaskWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSpuImportTask hubBrandDic) {
		return hubSpuImportTaskMapper.updateByPrimaryKeySelective(hubBrandDic);
	}

	public int updateByPrimaryKey(HubSpuImportTask hubBrandDic) {
		return hubSpuImportTaskMapper.updateByPrimaryKey(hubBrandDic);
	}

	 
}
