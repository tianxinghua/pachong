package com.shangpin.ephub.data.mysql.mapping.value.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.mapping.value.bean.HubSupplierValueMappingCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.mapping.value.bean.HubSupplierValueMappingWithCriteria;
import com.shangpin.ephub.data.mysql.mapping.value.mapper.HubSupplierValueMappingMapper;
import com.shangpin.ephub.data.mysql.mapping.value.po.HubSupplierValueMapping;
import com.shangpin.ephub.data.mysql.mapping.value.po.HubSupplierValueMappingCriteria;

/**
 * <p>Title:HubSupplierValueMappingService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:47:17
 */
@Service
public class HubSupplierValueMappingService {

	private HubSupplierValueMappingMapper hubSupplierValueMappingMapper;

	public int countByCriteria(HubSupplierValueMappingCriteria criteria) {
		return hubSupplierValueMappingMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSupplierValueMappingCriteria criteria) {
		return hubSupplierValueMappingMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long valueMappingId) {
		return hubSupplierValueMappingMapper.deleteByPrimaryKey(valueMappingId);
	}

	public int insert(HubSupplierValueMapping hubSupplierValueMapping) {
		return hubSupplierValueMappingMapper.insert(hubSupplierValueMapping);
	}

	public int insertSelective(HubSupplierValueMapping hubSupplierValueMapping) {
		return hubSupplierValueMappingMapper.insertSelective(hubSupplierValueMapping);
	}

	public List<HubSupplierValueMapping> selectByCriteriaWithRowbounds(
			HubSupplierValueMappingCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubSupplierValueMappingMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubSupplierValueMapping> selectByCriteria(HubSupplierValueMappingCriteria criteria) {
		return hubSupplierValueMappingMapper.selectByExample(criteria);
	}

	public HubSupplierValueMapping selectByPrimaryKey(Long valueMappingId) {
		return hubSupplierValueMappingMapper.selectByPrimaryKey(valueMappingId);
	}

	public int updateByCriteriaSelective(HubSupplierValueMappingWithCriteria hubSupplierValueMappingWithCriteria) {
		return hubSupplierValueMappingMapper.updateByExampleSelective(hubSupplierValueMappingWithCriteria.getHubSupplierValueMapping(), hubSupplierValueMappingWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubSupplierValueMappingWithCriteria hubSupplierValueMappingWithCriteria) {
		return hubSupplierValueMappingMapper.updateByExample(hubSupplierValueMappingWithCriteria.getHubSupplierValueMapping(), hubSupplierValueMappingWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSupplierValueMapping hubSupplierValueMapping) {
		return hubSupplierValueMappingMapper.updateByPrimaryKey(hubSupplierValueMapping);
	}

	public int updateByPrimaryKey(HubSupplierValueMapping hubSupplierValueMapping) {
		return hubSupplierValueMappingMapper.updateByPrimaryKey(hubSupplierValueMapping);
	}
}
