package com.shangpin.studio.data.mysql.service;


import com.shangpin.studio.data.mysql.bean.StudioUserCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioUserWithCriteria;
import com.shangpin.studio.data.mysql.mapper.StudioUserMapper;
import com.shangpin.studio.data.mysql.po.StudioUser;
import com.shangpin.studio.data.mysql.po.StudioUserCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
  StudioUser ����
 */
@Service
@Slf4j
public class StudioUserService {

	@Autowired
	private StudioUserMapper mapper;

	public int countByCriteria(StudioUserCriteria criteria) {
		return mapper.countByExample(criteria);
	}

	public int deleteByCriteria(StudioUserCriteria criteria) {
		return mapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long skuPendingId) {
		return mapper.deleteByPrimaryKey(skuPendingId);
	}

	public int insert(StudioUser obj) {
		return mapper.insert(obj);
	}

	public int insertSelective(StudioUser obj) {
		return mapper.insertSelective(obj);
	}

	public List<StudioUser> selectByCriteriaWithRowbounds(StudioUserCriteriaWithRowBounds criteriaWithRowBounds) {
		return mapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<StudioUser> selectByCriteria(StudioUserCriteria criteria) {
		return mapper.selectByExample(criteria);
	}

	public StudioUser selectByPrimaryKey(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	public int updateByCriteriaSelective(StudioUserWithCriteria criteria) {
		return mapper.updateByExampleSelective(criteria.getStudioUser(), criteria.getCriteria());
	}

	public int updateByCriteria(StudioUserWithCriteria criteria) {
		return mapper.updateByExample(criteria.getStudioUser(), criteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(StudioUser obj) {
		return mapper.updateByPrimaryKeySelective(obj);
	}

	public int updateByPrimaryKey(StudioUser obj) {
		return mapper.updateByPrimaryKey(obj);
	}



}
