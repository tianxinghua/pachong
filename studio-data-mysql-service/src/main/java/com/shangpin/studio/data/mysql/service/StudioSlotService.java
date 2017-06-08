package com.shangpin.studio.data.mysql.service;


import com.shangpin.studio.data.mysql.bean.StudioSlotCriteriaWithRowBounds;

import com.shangpin.studio.data.mysql.bean.StudioSlotWithCriteria;

import com.shangpin.studio.data.mysql.mapper.StudioSlotMapper;
import com.shangpin.studio.data.mysql.po.Studio;
import com.shangpin.studio.data.mysql.po.StudioSlot;
import com.shangpin.studio.data.mysql.po.StudioSlotCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
   studioSlot 服务
 */
@Service
@Slf4j
public class StudioSlotService {

	@Autowired
	private StudioSlotMapper mapper;

	public int countByCriteria(StudioSlotCriteria criteria) {
		return mapper.countByExample(criteria);
	}

	public int deleteByCriteria(StudioSlotCriteria criteria) {
		return mapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long skuPendingId) {
		return mapper.deleteByPrimaryKey(skuPendingId);
	}

	public int insert(StudioSlot studioSlot) {
		return mapper.insert(studioSlot);
	}

	public int insertSelective(StudioSlot studioSlot) {
		return mapper.insertSelective(studioSlot);
	}

	public List<StudioSlot> selectByCriteriaWithRowbounds(StudioSlotCriteriaWithRowBounds criteriaWithRowBounds) {
		return mapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<StudioSlot> selectByCriteria(StudioSlotCriteria criteria) {
		return mapper.selectByExample(criteria);
	}

	public StudioSlot selectByPrimaryKey(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	public int updateByCriteriaSelective(StudioSlotWithCriteria studioSlotWithCriteria) {
		return mapper.updateByExampleSelective(studioSlotWithCriteria.getStudioSlot(), studioSlotWithCriteria.getCriteria());
	}

	public int updateByCriteria(StudioSlotWithCriteria studioSlotWithCriteria) {
		return mapper.updateByExample(studioSlotWithCriteria.getStudioSlot(), studioSlotWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(StudioSlot studioSlot) {
		return mapper.updateByPrimaryKeySelective(studioSlot);
	}

	public int updateByPrimaryKey(StudioSlot studioSlot) {
		return mapper.updateByPrimaryKey(studioSlot);
	}



}
