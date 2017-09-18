package com.shangpin.ephub.data.mysql.sp.spshippingpolicydetail.service;



import com.shangpin.ephub.data.mysql.sp.spshippingpolicydetail.bean.SpShippingPolicyDetailCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.sp.spshippingpolicydetail.bean.SpShippingPolicyDetailWithCriteria;
import com.shangpin.ephub.data.mysql.sp.spshippingpolicydetail.mapper.SpShippingPolicyDetailMapper;
import com.shangpin.ephub.data.mysql.sp.spshippingpolicydetail.po.SpShippingPolicyDetail;
import com.shangpin.ephub.data.mysql.sp.spshippingpolicydetail.po.SpShippingPolicyDetailCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
  SpShippingPolicyDetail
 */
@Service
@Slf4j
public class SpShippingPolicyDetailService {

	@Autowired
	private SpShippingPolicyDetailMapper mapper;

	public int countByCriteria(SpShippingPolicyDetailCriteria criteria) {
		return mapper.countByExample(criteria);
	}

	public int deleteByCriteria(SpShippingPolicyDetailCriteria criteria) {
		return mapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long skuPendingId) {
		return mapper.deleteByPrimaryKey(skuPendingId);
	}

	public int insert(SpShippingPolicyDetail obj) {
		return mapper.insert(obj);
	}

	public int insertSelective(SpShippingPolicyDetail obj) {
		return mapper.insertSelective(obj);
	}

	public List<SpShippingPolicyDetail> selectByCriteriaWithRowbounds(SpShippingPolicyDetailCriteriaWithRowBounds criteriaWithRowBounds) {
		return mapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<SpShippingPolicyDetail> selectByCriteria(SpShippingPolicyDetailCriteria criteria) {
		return mapper.selectByExample(criteria);
	}

	public SpShippingPolicyDetail selectByPrimaryKey(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	public int updateByCriteriaSelective(SpShippingPolicyDetailWithCriteria criteria) {
		return mapper.updateByExampleSelective(criteria.getSpShippingPolicyDetail(), criteria.getCriteria());
	}

	public int updateByCriteria(SpShippingPolicyDetailWithCriteria criteria) {
		return mapper.updateByExample(criteria.getSpShippingPolicyDetail(), criteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(SpShippingPolicyDetail obj) {
		return mapper.updateByPrimaryKeySelective(obj);
	}

	public int updateByPrimaryKey(SpShippingPolicyDetail obj) {
		return mapper.updateByPrimaryKey(obj);
	}



}
