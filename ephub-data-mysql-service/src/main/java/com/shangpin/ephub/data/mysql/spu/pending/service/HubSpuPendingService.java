package com.shangpin.ephub.data.mysql.spu.pending.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.spu.pending.bean.HubSpuPendingCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.spu.pending.bean.HubSpuPendingWithCriteria;
import com.shangpin.ephub.data.mysql.spu.pending.mapper.HubSpuPendingMapper;
import com.shangpin.ephub.data.mysql.spu.pending.po.HubSpuPending;
import com.shangpin.ephub.data.mysql.spu.pending.po.HubSpuPendingCriteria;

/**
 * <p>Title:HubSpuPendingService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:19:15
 */
@Service
public class HubSpuPendingService {

	@Autowired
	private HubSpuPendingMapper hubSpuPendingMapper;

	public int countByCriteria(HubSpuPendingCriteria criteria) {
		return hubSpuPendingMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSpuPendingCriteria criteria) {
		return hubSpuPendingMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long spuPendingId) {
		return hubSpuPendingMapper.deleteByPrimaryKey(spuPendingId);
	}

	public int insert(HubSpuPending hubSpuPending) {
		return hubSpuPendingMapper.insert(hubSpuPending);
	}

	public int insertSelective(HubSpuPending hubSpuPending) {
		return hubSpuPendingMapper.insertSelective(hubSpuPending);
	}

	public List<HubSpuPending> selectByCriteriaWithRowbounds(HubSpuPendingCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubSpuPendingMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubSpuPending> selectByCriteria(HubSpuPendingCriteria criteria) {
		return hubSpuPendingMapper.selectByExample(criteria);
	}

	public HubSpuPending selectByPrimaryKey(Long spuPendingId) {
		return hubSpuPendingMapper.selectByPrimaryKey(spuPendingId);
	}

	public int updateByCriteriaSelective(HubSpuPendingWithCriteria hubSpuPendingWithCriteria) {
		return hubSpuPendingMapper.updateByExampleSelective(hubSpuPendingWithCriteria.getHubSpuPending(), hubSpuPendingWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubSpuPendingWithCriteria hubSpuPendingWithCriteria) {
		return hubSpuPendingMapper.updateByExample(hubSpuPendingWithCriteria.getHubSpuPending(), hubSpuPendingWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSpuPending hubSpuPending) {
		return hubSpuPendingMapper.updateByPrimaryKeySelective(hubSpuPending);
	}

	public int updateByPrimaryKey(HubSpuPending hubSpuPending) {
		return hubSpuPendingMapper.updateByPrimaryKey(hubSpuPending);
	}


	public int countDistinctBrandNoAndSpuModelByCriteria(HubSpuPendingCriteria criteria) {

		return hubSpuPendingMapper.countDistinctBrandNoAndSpuModelByExample(criteria);
	}

}
