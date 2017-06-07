package com.shangpin.studio.data.mysql.service;


import com.shangpin.studio.data.mysql.bean.StudioSlotReturnDetailCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioSlotReturnDetailWithCriteria;
import com.shangpin.studio.data.mysql.bean.StudioSlotReturnMasterCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioSlotReturnMasterWithCriteria;
import com.shangpin.studio.data.mysql.mapper.StudioSlotReturnDetailMapper;
import com.shangpin.studio.data.mysql.mapper.StudioSlotReturnMasterMapper;
import com.shangpin.studio.data.mysql.po.StudioSlotReturnDetail;
import com.shangpin.studio.data.mysql.po.StudioSlotReturnDetailCriteria;
import com.shangpin.studio.data.mysql.po.StudioSlotReturnMasterCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 StudioSlotReturnDetail 服务
 */
@Service
@Slf4j
public class StudioSlotReturnDetailService {

	@Autowired
	private StudioSlotReturnDetailMapper mapper;

	public int countByCriteria(StudioSlotReturnDetailCriteria criteria) {
		return mapper.countByExample(criteria);
	}

	public int deleteByCriteria(StudioSlotReturnDetailCriteria criteria) {
		return mapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long id) {
		return mapper.deleteByPrimaryKey(id);
	}

	public int insert(StudioSlotReturnDetail obj) {
		return mapper.insert(obj);
	}

	public int insertSelective(StudioSlotReturnDetail obj) {
		return mapper.insertSelective(obj);
	}

	public List<StudioSlotReturnDetail> selectByCriteriaWithRowbounds(StudioSlotReturnDetailCriteriaWithRowBounds criteriaWithRowBounds) {
		return mapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<StudioSlotReturnDetail> selectByCriteria(StudioSlotReturnDetailCriteria criteria) {
		return mapper.selectByExample(criteria);
	}

	public StudioSlotReturnDetail selectByPrimaryKey(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	public int updateByCriteriaSelective(StudioSlotReturnDetailWithCriteria criteria) {
		return mapper.updateByExampleSelective(criteria.getStudioSlotReturnDetail(), criteria.getCriteria());
	}

	public int updateByCriteria(StudioSlotReturnDetailWithCriteria criteria) {
		return mapper.updateByExample(criteria.getStudioSlotReturnDetail(), criteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(StudioSlotReturnDetail obj) {
		return mapper.updateByPrimaryKeySelective(obj);
	}

	public int updateByPrimaryKey(StudioSlotReturnDetail obj) {
		return mapper.updateByPrimaryKey(obj);
	}



}
