package com.shangpin.ephub.data.mysql.mapping.column.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.mapping.column.bean.HubSupplierColMappingCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.mapping.column.bean.HubSupplierColMappingWithCriteria;
import com.shangpin.ephub.data.mysql.mapping.column.mapper.HubSupplierColMappingMapper;
import com.shangpin.ephub.data.mysql.mapping.column.po.HubSupplierColMapping;
import com.shangpin.ephub.data.mysql.mapping.column.po.HubSupplierColMappingCriteria;

/**
 * <p>Title:HubSupplierColMappingService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:39:02
 */
@Service
public class HubSupplierColMappingService {

	@Autowired
	private HubSupplierColMappingMapper hubSupplierColMappingMapper;

	public int countByCriteria(HubSupplierColMappingCriteria criteria) {
		return hubSupplierColMappingMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSupplierColMappingCriteria criteria) {
		return hubSupplierColMappingMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long supplierColMappingId) {
		return hubSupplierColMappingMapper.deleteByPrimaryKey(supplierColMappingId);
	}

	public int insert(HubSupplierColMapping hubSupplierColMapping) {
		return hubSupplierColMappingMapper.insert(hubSupplierColMapping);
	}

	public int insertSelective(HubSupplierColMapping hubSupplierColMapping) {
		return hubSupplierColMappingMapper.insertSelective(hubSupplierColMapping);
	}

	public List<HubSupplierColMapping> selectByCriteriaWithRowbounds(
			HubSupplierColMappingCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubSupplierColMappingMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubSupplierColMapping> selectByCriteria(HubSupplierColMappingCriteria criteria) {
		return hubSupplierColMappingMapper.selectByExample(criteria);
	}

	public HubSupplierColMapping selectByPrimaryKey(Long supplierColMappingId) {
		return hubSupplierColMappingMapper.selectByPrimaryKey(supplierColMappingId);
	}

	public int updateByCriteriaSelective(HubSupplierColMappingWithCriteria hubSupplierColMappingWithCriteria) {
		return hubSupplierColMappingMapper.updateByExampleSelective(hubSupplierColMappingWithCriteria.getHubSupplierColMapping(), hubSupplierColMappingWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubSupplierColMappingWithCriteria hubSupplierColMappingWithCriteria) {
		return hubSupplierColMappingMapper.updateByExample(hubSupplierColMappingWithCriteria.getHubSupplierColMapping(), hubSupplierColMappingWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSupplierColMapping hubSupplierColMapping) {
		return hubSupplierColMappingMapper.updateByPrimaryKeySelective(hubSupplierColMapping);
	}

	public int updateByPrimaryKey(HubSupplierColMapping hubSupplierColMapping) {
		return hubSupplierColMappingMapper.updateByPrimaryKey(hubSupplierColMapping);
	}
}
