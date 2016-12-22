package com.shangpin.ephub.data.mysql.mapping.material.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.mapping.material.bean.HubMaterialMappingCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.mapping.material.bean.HubMaterialMappingWithCriteria;
import com.shangpin.ephub.data.mysql.mapping.material.mapper.HubMaterialMappingMapper;
import com.shangpin.ephub.data.mysql.mapping.material.po.HubMaterialMapping;
import com.shangpin.ephub.data.mysql.mapping.material.po.HubMaterialMappingCriteria;

/**
 * <p>Title:HubMaterialMappingService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月22日 上午11:52:32
 */
@Service
public class HubMaterialMappingService {

	@Autowired
	private HubMaterialMappingMapper hubMaterialMappingMapper;

	public int countByCriteria(HubMaterialMappingCriteria criteria) {
		return hubMaterialMappingMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubMaterialMappingCriteria criteria) {
		return hubMaterialMappingMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long materialMappingId) {
		return hubMaterialMappingMapper.deleteByPrimaryKey(materialMappingId);
	}

	public int insert(HubMaterialMapping hubMaterialMapping) {
		return hubMaterialMappingMapper.insert(hubMaterialMapping);
	}

	public int insertSelective(HubMaterialMapping hubMaterialMapping) {
		return hubMaterialMappingMapper.insertSelective(hubMaterialMapping);
	}

	public List<HubMaterialMapping> selectByCriteriaWithRowbounds(
			HubMaterialMappingCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubMaterialMappingMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubMaterialMapping> selectByCriteria(HubMaterialMappingCriteria criteria) {
		return hubMaterialMappingMapper.selectByExample(criteria);
	}

	public HubMaterialMapping selectByPrimaryKey(Long materialMappingId) {
		return hubMaterialMappingMapper.selectByPrimaryKey(materialMappingId);
	}

	public int updateByCriteriaSelective(HubMaterialMappingWithCriteria hubMaterialMappingWithCriteria) {
		return hubMaterialMappingMapper.updateByExampleSelective(hubMaterialMappingWithCriteria.getHubMaterialMapping(), hubMaterialMappingWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubMaterialMappingWithCriteria hubMaterialMappingWithCriteria) {
		return hubMaterialMappingMapper.updateByExample(hubMaterialMappingWithCriteria.getHubMaterialMapping(), hubMaterialMappingWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubMaterialMapping hubMaterialMapping) {
		return hubMaterialMappingMapper.updateByPrimaryKeySelective(hubMaterialMapping);
	}

	public int updateByPrimaryKey(HubMaterialMapping hubMaterialMapping) {
		return hubMaterialMappingMapper.updateByPrimaryKey(hubMaterialMapping);
	}
}
