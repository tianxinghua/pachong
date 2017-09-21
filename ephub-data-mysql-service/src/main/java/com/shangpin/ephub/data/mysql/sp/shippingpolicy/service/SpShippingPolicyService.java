package com.shangpin.ephub.data.mysql.sp.shippingpolicy.service;




import com.shangpin.ephub.data.mysql.sp.shippingpolicy.bean.SpShippingPolicyCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.sp.shippingpolicy.bean.SpShippingPolicyWithCriteria;
import com.shangpin.ephub.data.mysql.sp.shippingpolicy.mapper.SpShippingPolicyMapper;
import com.shangpin.ephub.data.mysql.sp.shippingpolicy.po.SpShippingPolicy;
import com.shangpin.ephub.data.mysql.sp.shippingpolicy.po.SpShippingPolicyCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
  SpShippingPolicy
 */
@Service
@Slf4j
public class SpShippingPolicyService {

	@Autowired
	private SpShippingPolicyMapper mapper;

	public int countByCriteria(SpShippingPolicyCriteria criteria) {
		return mapper.countByExample(criteria);
	}

	public int deleteByCriteria(SpShippingPolicyCriteria criteria) {
		return mapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long skuPendingId) {
		return mapper.deleteByPrimaryKey(skuPendingId);
	}

	public int insert(SpShippingPolicy obj) {
		return mapper.insert(obj);
	}

	public int insertSelective(SpShippingPolicy obj) {
		return mapper.insertSelective(obj);
	}

	public List<SpShippingPolicy> selectByCriteriaWithRowbounds(SpShippingPolicyCriteriaWithRowBounds criteriaWithRowBounds) {
		return mapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<SpShippingPolicy> selectByCriteria(SpShippingPolicyCriteria criteria) {
		return mapper.selectByExample(criteria);
	}

	public SpShippingPolicy selectByPrimaryKey(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	public int updateByCriteriaSelective(SpShippingPolicyWithCriteria criteria) {
		return mapper.updateByExampleSelective(criteria.getSpShippingPolicy(), criteria.getCriteria());
	}

	public int updateByCriteria(SpShippingPolicyWithCriteria criteria) {
		return mapper.updateByExample(criteria.getSpShippingPolicy(), criteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(SpShippingPolicy obj) {
		return mapper.updateByPrimaryKeySelective(obj);
	}

	public int updateByPrimaryKey(SpShippingPolicy obj) {
		return mapper.updateByPrimaryKey(obj);
	}



}
