package com.shangpin.ephub.data.mysql.sku.hub.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.sku.hub.bean.HubSkuCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.sku.hub.bean.HubSkuWithCriteria;
import com.shangpin.ephub.data.mysql.sku.hub.mapper.HubSkuMapper;
import com.shangpin.ephub.data.mysql.sku.hub.po.HubSku;
import com.shangpin.ephub.data.mysql.sku.hub.po.HubSkuCriteria;

/**
 * <p>Title:HubSkuService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:09:16
 */
@Service
public class HubSkuService {

	@Autowired
	private HubSkuMapper hubSkuMapper;

	public int countByCriteria(HubSkuCriteria criteria) {
		return hubSkuMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSkuCriteria criteria) {
		return hubSkuMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long skuId) {
		return hubSkuMapper.deleteByPrimaryKey(skuId);
	}

	public int insert(HubSku hubSku) {
		return hubSkuMapper.insert(hubSku);
	}

	public int insertSelective(HubSku hubSku) {
		return hubSkuMapper.insertSelective(hubSku);
	}

	public List<HubSku> selectByCriteriaWithRowbounds(HubSkuCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubSkuMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubSku> selectByCriteria(HubSkuCriteria criteria) {
		return hubSkuMapper.selectByExample(criteria);
	}

	public HubSku selectByPrimaryKey(Long skuId) {
		return hubSkuMapper.selectByPrimaryKey(skuId);
	}

	public int updateByCriteriaSelective(HubSkuWithCriteria hubSkuWithCriteria) {
		return hubSkuMapper.updateByExampleSelective(hubSkuWithCriteria.getHubSku(), hubSkuWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubSkuWithCriteria hubSkuWithCriteria) {
		return hubSkuMapper.updateByExample(hubSkuWithCriteria.getHubSku(), hubSkuWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSku hubSku) {
		return hubSkuMapper.updateByPrimaryKeySelective(hubSku);
	}

	public int updateByPrimaryKey(HubSku hubSku) {
		return hubSkuMapper.updateByPrimaryKey(hubSku);
	}
}
