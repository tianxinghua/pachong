package com.shangpin.ephub.data.mysql.supplier.config.servcie;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.supplier.config.bean.HubSupplierConfigCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.supplier.config.bean.HubSupplierConfigWithCriteria;
import com.shangpin.ephub.data.mysql.supplier.config.mapper.HubSupplierConfigMapper;
import com.shangpin.ephub.data.mysql.supplier.config.po.HubSupplierConfig;
import com.shangpin.ephub.data.mysql.supplier.config.po.HubSupplierConfigCriteria;

/**
 * <p>Title:HubSupplierConfigService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:22:51
 */
@Service
public class HubSupplierConfigService {

	@Autowired
	private HubSupplierConfigMapper hubSupplierConfigMapper;

	public int countByCriteria(HubSupplierConfigCriteria criteria) {
		return hubSupplierConfigMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSupplierConfigCriteria criteria) {
		return hubSupplierConfigMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long supplierConfigId) {
		return hubSupplierConfigMapper.deleteByPrimaryKey(supplierConfigId);
	}

	public int insert(HubSupplierConfig hubSupplierConfig) {
		return hubSupplierConfigMapper.insert(hubSupplierConfig);
	}

	public int insertSelective(HubSupplierConfig hubSupplierConfig) {
		return hubSupplierConfigMapper.insertSelective(hubSupplierConfig);
	}

	public List<HubSupplierConfig> selectByCriteriaWithRowbounds(
			HubSupplierConfigCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubSupplierConfigMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubSupplierConfig> selectByCriteria(HubSupplierConfigCriteria criteria) {
		return hubSupplierConfigMapper.selectByExample(criteria);
	}

	public HubSupplierConfig selectByPrimaryKey(Long supplierConfigId) {
		return hubSupplierConfigMapper.selectByPrimaryKey(supplierConfigId);
	}

	public int updateByCriteriaSelective(HubSupplierConfigWithCriteria hubSupplierConfigWithCriteria) {
		return hubSupplierConfigMapper.updateByExampleSelective(hubSupplierConfigWithCriteria.getHubSupplierConfig(), hubSupplierConfigWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubSupplierConfigWithCriteria hubSupplierConfigWithCriteria) {
		return hubSupplierConfigMapper.updateByExample(hubSupplierConfigWithCriteria.getHubSupplierConfig(), hubSupplierConfigWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSupplierConfig hubBrandDic) {
		return hubSupplierConfigMapper.updateByPrimaryKeySelective(hubBrandDic);
	}

	public int updateByPrimaryKey(HubSupplierConfig hubBrandDic) {
		return hubSupplierConfigMapper.updateByPrimaryKey(hubBrandDic);
	}
}
