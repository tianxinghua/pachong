package com.shangpin.ephub.data.mysql.spu.pendingnohand.service;



import com.shangpin.ephub.data.mysql.spu.pendingnohand.bean.HubSpuPendingNohandleReasonCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.spu.pendingnohand.bean.HubSpuPendingNohandleReasonWithCriteria;
import com.shangpin.ephub.data.mysql.spu.pendingnohand.mapper.HubSpuPendingNohandleReasonMapper;
import com.shangpin.ephub.data.mysql.spu.pendingnohand.po.HubSpuPendingNohandleReason;
import com.shangpin.ephub.data.mysql.spu.pendingnohand.po.HubSpuPendingNohandleReasonCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
  HubSpuPendingNohandleReason
 */
@Service
@Slf4j
public class HubSpuPendingNohandleReasonService {

	@Autowired
	private HubSpuPendingNohandleReasonMapper mapper;

	public int countByCriteria(HubSpuPendingNohandleReasonCriteria criteria) {
		return mapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSpuPendingNohandleReasonCriteria criteria) {
		return mapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long skuPendingId) {
		return mapper.deleteByPrimaryKey(skuPendingId);
	}

	public int insert(HubSpuPendingNohandleReason obj) {
		return mapper.insert(obj);
	}

	public int insertSelective(HubSpuPendingNohandleReason obj) {
		return mapper.insertSelective(obj);
	}

	public List<HubSpuPendingNohandleReason> selectByCriteriaWithRowbounds(HubSpuPendingNohandleReasonCriteriaWithRowBounds criteriaWithRowBounds) {
		return mapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubSpuPendingNohandleReason> selectByCriteria(HubSpuPendingNohandleReasonCriteria criteria) {
		return mapper.selectByExample(criteria);
	}

	public HubSpuPendingNohandleReason selectByPrimaryKey(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	public int updateByCriteriaSelective(HubSpuPendingNohandleReasonWithCriteria criteria) {
		return mapper.updateByExampleSelective(criteria.getHubSpuPendingNohandleReason(), criteria.getCriteria());
	}

	public int updateByCriteria(HubSpuPendingNohandleReasonWithCriteria criteria) {
		return mapper.updateByExample(criteria.getHubSpuPendingNohandleReason(), criteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSpuPendingNohandleReason obj) {
		return mapper.updateByPrimaryKeySelective(obj);
	}

	public int updateByPrimaryKey(HubSpuPendingNohandleReason obj) {
		return mapper.updateByPrimaryKey(obj);
	}



}
