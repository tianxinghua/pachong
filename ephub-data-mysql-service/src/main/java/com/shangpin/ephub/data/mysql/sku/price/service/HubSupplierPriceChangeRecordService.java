package com.shangpin.ephub.data.mysql.sku.price.service;

import com.shangpin.ephub.data.mysql.sku.price.bean.HubSupplierPriceChangeRecordWithCriteria;
import com.shangpin.ephub.data.mysql.sku.price.bean.HubSupplierPriceChangeRecordWithRowBounds;

import com.shangpin.ephub.data.mysql.sku.price.mapper.HubSupplierPriceChangeRecordMapper;
import com.shangpin.ephub.data.mysql.sku.price.po.HubSupplierPriceChangeRecord;
import com.shangpin.ephub.data.mysql.sku.price.po.HubSupplierPriceChangeRecordCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**

 * <p>Description: 价格简单业务服务 </p>
 *
 */
@Service
public class HubSupplierPriceChangeRecordService {

	@Autowired
	private HubSupplierPriceChangeRecordMapper hubSupplierPriceChangeRecordMapper;

	public int countByCriteria(HubSupplierPriceChangeRecordCriteria criteria) {
		return hubSupplierPriceChangeRecordMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSupplierPriceChangeRecordCriteria criteria) {
		return hubSupplierPriceChangeRecordMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long supplierPriceChangeRecordId) {
		return hubSupplierPriceChangeRecordMapper.deleteByPrimaryKey(supplierPriceChangeRecordId);
	}

	public int insert(HubSupplierPriceChangeRecord hubSupplierPriceChangeRecord) {
		return hubSupplierPriceChangeRecordMapper.insert(hubSupplierPriceChangeRecord);
	}

	public int insertSelective(HubSupplierPriceChangeRecord hubSupplierPriceChangeRecord) {
		return hubSupplierPriceChangeRecordMapper.insertSelective(hubSupplierPriceChangeRecord);
	}

	public List<HubSupplierPriceChangeRecord> selectByCriteria(HubSupplierPriceChangeRecordCriteria criteria) {
		return hubSupplierPriceChangeRecordMapper.selectByExample(criteria);
	}

	public List<HubSupplierPriceChangeRecord> selectByCriteriaWithRowbounds(
			HubSupplierPriceChangeRecordWithRowBounds criteriaWithRowBounds) {
		return hubSupplierPriceChangeRecordMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public HubSupplierPriceChangeRecord selectByPrimaryKey(Long supplierPriceChangeRecordId) {
		return hubSupplierPriceChangeRecordMapper.selectByPrimaryKey(supplierPriceChangeRecordId);
	}

	public int updateByCriteriaSelective(HubSupplierPriceChangeRecordWithCriteria hubSupplierPriceChangeRecordWithCriteria) {
		return hubSupplierPriceChangeRecordMapper.updateByExampleSelective(hubSupplierPriceChangeRecordWithCriteria.getHubSupplierPriceChangeRecord(), hubSupplierPriceChangeRecordWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubSupplierPriceChangeRecordWithCriteria hubSupplierPriceChangeRecordWithCriteria) {
		return hubSupplierPriceChangeRecordMapper.updateByExample(hubSupplierPriceChangeRecordWithCriteria.getHubSupplierPriceChangeRecord(), hubSupplierPriceChangeRecordWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSupplierPriceChangeRecord hubSupplierPriceChangeRecord) {
		return hubSupplierPriceChangeRecordMapper.updateByPrimaryKeySelective(hubSupplierPriceChangeRecord);
	}

	public int updateByPrimaryKey(HubSupplierPriceChangeRecord hubSupplierPriceChangeRecord) {
		return hubSupplierPriceChangeRecordMapper.updateByPrimaryKey(hubSupplierPriceChangeRecord);
	}
}
