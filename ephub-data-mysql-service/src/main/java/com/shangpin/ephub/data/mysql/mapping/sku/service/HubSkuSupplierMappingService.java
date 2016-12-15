package com.shangpin.ephub.data.mysql.mapping.sku.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.mapping.sku.bean.HubSkuSupplierMappingCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.mapping.sku.bean.HubSkuSupplierMappingWithCriteria;
import com.shangpin.ephub.data.mysql.mapping.sku.mapper.HubSkuSupplierMappingMapper;
import com.shangpin.ephub.data.mysql.mapping.sku.po.HubSkuSupplierMapping;
import com.shangpin.ephub.data.mysql.mapping.sku.po.HubSkuSupplierMappingCriteria;

/**
 * <p>Title:HubSkuSupplierMappingService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:41:29
 */
@Service
public class HubSkuSupplierMappingService {

	@Autowired
	private HubSkuSupplierMappingMapper hubSkuSupplierMappingMapper;

	public int countByCriteria(HubSkuSupplierMappingCriteria criteria) {
		return hubSkuSupplierMappingMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSkuSupplierMappingCriteria criteria) {
		return hubSkuSupplierMappingMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long skuSupplierMappingId) {
		return hubSkuSupplierMappingMapper.deleteByPrimaryKey(skuSupplierMappingId);
	}

	public int insert(HubSkuSupplierMapping hubSkuSupplierMapping) {
		return hubSkuSupplierMappingMapper.insert(hubSkuSupplierMapping);
	}

	public int insertSelective(HubSkuSupplierMapping hubSkuSupplierMapping) {
		return hubSkuSupplierMappingMapper.insertSelective(hubSkuSupplierMapping);
	}

	public List<HubSkuSupplierMapping> selectByCriteriaWithRowbounds(
			HubSkuSupplierMappingCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubSkuSupplierMappingMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubSkuSupplierMapping> selectByCriteria(HubSkuSupplierMappingCriteria criteria) {
		return hubSkuSupplierMappingMapper.selectByExample(criteria);
	}

	public HubSkuSupplierMapping selectByPrimaryKey(Long skuSupplierMappingId) {
		return hubSkuSupplierMappingMapper.selectByPrimaryKey(skuSupplierMappingId);
	}

	public int updateByCriteriaSelective(HubSkuSupplierMappingWithCriteria hubSkuSupplierMappingWithCriteria) {
		return hubSkuSupplierMappingMapper.updateByExampleSelective(hubSkuSupplierMappingWithCriteria.getHubSkuSupplierMapping(), hubSkuSupplierMappingWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubSkuSupplierMappingWithCriteria hubSkuSupplierMappingWithCriteria) {
		return hubSkuSupplierMappingMapper.updateByExample(hubSkuSupplierMappingWithCriteria.getHubSkuSupplierMapping(), hubSkuSupplierMappingWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSkuSupplierMapping hubBrandDic) {
		return hubSkuSupplierMappingMapper.updateByPrimaryKeySelective(hubBrandDic);
	}

	public int updateByPrimaryKey(HubSkuSupplierMapping hubBrandDic) {
		return hubSkuSupplierMappingMapper.updateByPrimaryKey(hubBrandDic);
	}
	
}
