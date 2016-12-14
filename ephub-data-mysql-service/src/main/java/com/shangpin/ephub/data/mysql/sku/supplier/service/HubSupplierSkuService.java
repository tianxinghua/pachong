package com.shangpin.ephub.data.mysql.sku.supplier.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.sku.supplier.bean.HubSupplierSkuCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.sku.supplier.bean.HubSupplierSkuWithCriteria;
import com.shangpin.ephub.data.mysql.sku.supplier.mapper.HubSupplierSkuMapper;
import com.shangpin.ephub.data.mysql.sku.supplier.po.HubSupplierSku;
import com.shangpin.ephub.data.mysql.sku.supplier.po.HubSupplierSkuCriteria;

/**
 * <p>Title:HubSupplierSkuService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:14:20
 */
@Service
public class HubSupplierSkuService {

	@Autowired
	private HubSupplierSkuMapper hubSupplierSkuMapper;

	public int countByCriteria(HubSupplierSkuCriteria criteria) {
		return hubSupplierSkuMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSupplierSkuCriteria criteria) {
		return hubSupplierSkuMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long supplierSkuId) {
		return hubSupplierSkuMapper.deleteByPrimaryKey(supplierSkuId);
	}

	public int insert(HubSupplierSku hubSupplierSku) {
		return hubSupplierSkuMapper.insert(hubSupplierSku);
	}

	public int insertSelective(HubSupplierSku hubSupplierSku) {
		return hubSupplierSkuMapper.insertSelective(hubSupplierSku);
	}

	public List<HubSupplierSku> selectByCriteria(HubSupplierSkuCriteria criteria) {
		return hubSupplierSkuMapper.selectByExample(criteria);
	}

	public List<HubSupplierSku> selectByCriteriaWithRowbounds(
			HubSupplierSkuCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubSupplierSkuMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public HubSupplierSku selectByPrimaryKey(Long supplierSkuId) {
		return hubSupplierSkuMapper.selectByPrimaryKey(supplierSkuId);
	}

	public int updateByCriteriaSelective(HubSupplierSkuWithCriteria hubSupplierSkuWithCriteria) {
		return hubSupplierSkuMapper.updateByExampleSelective(hubSupplierSkuWithCriteria.getHubSupplierSku(), hubSupplierSkuWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubSupplierSkuWithCriteria hubSupplierSkuWithCriteria) {
		return hubSupplierSkuMapper.updateByExample(hubSupplierSkuWithCriteria.getHubSupplierSku(), hubSupplierSkuWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSupplierSku hubSupplierSku) {
		return hubSupplierSkuMapper.updateByPrimaryKeySelective(hubSupplierSku);
	}

	public int updateByPrimaryKey(HubSupplierSku hubSupplierSku) {
		return hubSupplierSkuMapper.updateByPrimaryKey(hubSupplierSku);
	}
}
