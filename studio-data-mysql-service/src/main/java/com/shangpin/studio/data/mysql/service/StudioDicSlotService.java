package com.shangpin.studio.data.mysql.service;


import com.shangpin.studio.data.mysql.bean.StudioDicSlotCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioDicSlotWithCriteria;
import com.shangpin.studio.data.mysql.mapper.StudioDicSlotMapper;

import com.shangpin.studio.data.mysql.po.dic.StudioDicSlot;
import com.shangpin.studio.data.mysql.po.dic.StudioDicSlotCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 StudioDicSlotService
 */
@Service
@Slf4j
public class StudioDicSlotService {

	@Autowired
	private StudioDicSlotMapper mapper;

	public int countByCriteria(StudioDicSlotCriteria criteria) {
		return mapper.countByExample(criteria);
	}

	public int deleteByCriteria(StudioDicSlotCriteria criteria) {
		return mapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long skuPendingId) {
		return mapper.deleteByPrimaryKey(skuPendingId);
	}

	public int insert(StudioDicSlot obj) {
		return mapper.insert(obj);
	}

	public int insertSelective(StudioDicSlot obj) {
		return mapper.insertSelective(obj);
	}

	public List<StudioDicSlot> selectByCriteriaWithRowbounds(StudioDicSlotCriteriaWithRowBounds criteriaWithRowBounds) {
		return mapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<StudioDicSlot> selectByCriteria(StudioDicSlotCriteria criteria) {
		return mapper.selectByExample(criteria);
	}

	public StudioDicSlot selectByPrimaryKey(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	public int updateByCriteriaSelective(StudioDicSlotWithCriteria criteria) {
		return mapper.updateByExampleSelective(criteria.getStudioDicSlot(), criteria.getCriteria());
	}

	public int updateByCriteria(StudioDicSlotWithCriteria criteria) {
		return mapper.updateByExample(criteria.getStudioDicSlot(), criteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(StudioDicSlot obj) {
		return mapper.updateByPrimaryKeySelective(obj);
	}

	public int updateByPrimaryKey(StudioDicSlot obj) {
		return mapper.updateByPrimaryKey(obj);
	}



}
