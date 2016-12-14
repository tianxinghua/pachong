package com.shangpin.ephub.data.mysql.sku.pending.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.sku.pending.bean.HubSkuPendingCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.sku.pending.bean.HubSkuPendingWithCriteria;
import com.shangpin.ephub.data.mysql.sku.pending.mapper.HubSkuPendingMapper;
import com.shangpin.ephub.data.mysql.sku.pending.po.HubSkuPending;
import com.shangpin.ephub.data.mysql.sku.pending.po.HubSkuPendingCriteria;

/**
 * <p>Title:HubSkuPendingService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:12:15
 */
@Service
public class HubSkuPendingService {

	@Autowired
	private HubSkuPendingMapper hubSkuPendingMapper;

	public int countByCriteria(HubSkuPendingCriteria criteria) {
		return hubSkuPendingMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSkuPendingCriteria criteria) {
		return hubSkuPendingMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long skuPendingId) {
		return hubSkuPendingMapper.deleteByPrimaryKey(skuPendingId);
	}

	public int insert(HubSkuPending hubSkuPending) {
		return hubSkuPendingMapper.insert(hubSkuPending);
	}

	public int insertSelective(HubSkuPending hubSkuPending) {
		return hubSkuPendingMapper.insertSelective(hubSkuPending);
	}

	public List<HubSkuPending> selectByCriteriaWithRowbounds(HubSkuPendingCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubSkuPendingMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubSkuPending> selectByCriteria(HubSkuPendingCriteria criteria) {
		return hubSkuPendingMapper.selectByExample(criteria);
	}

	public HubSkuPending selectByPrimaryKey(Long skuPendingId) {
		return hubSkuPendingMapper.selectByPrimaryKey(skuPendingId);
	}

	public int updateByCriteriaSelective(HubSkuPendingWithCriteria hubSkuPendingWithCriteria) {
		return hubSkuPendingMapper.updateByExampleSelective(hubSkuPendingWithCriteria.getHubSkuPending(), hubSkuPendingWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubSkuPendingWithCriteria hubSkuPendingWithCriteria) {
		return hubSkuPendingMapper.updateByExample(hubSkuPendingWithCriteria.getHubSkuPending(), hubSkuPendingWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSkuPending hubSkuPending) {
		return hubSkuPendingMapper.updateByPrimaryKeySelective(hubSkuPending);
	}

	public int updateByPrimaryKey(HubSkuPending hubSkuPending) {
		return hubSkuPendingMapper.updateByPrimaryKey(hubSkuPending);
	}
}
