package com.shangpin.studio.data.mysql.service;


import com.shangpin.studio.data.mysql.bean.StudioDicCalendarCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioDicCalendarWithCriteria;
import com.shangpin.studio.data.mysql.mapper.StudioDicCalendarMapper;

import com.shangpin.studio.data.mysql.po.dic.StudioDicCalendar;
import com.shangpin.studio.data.mysql.po.dic.StudioDicCalendarCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
  StudioDicCalendar ����
 */
@Service
@Slf4j
public class StudioDicCalendarService {

	@Autowired
	private StudioDicCalendarMapper mapper;

	public int countByCriteria(StudioDicCalendarCriteria criteria) {
		return mapper.countByExample(criteria);
	}

	public int deleteByCriteria(StudioDicCalendarCriteria criteria) {
		return mapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long skuPendingId) {
		return mapper.deleteByPrimaryKey(skuPendingId);
	}

	public int insert(StudioDicCalendar obj) {
		return mapper.insert(obj);
	}

	public int insertSelective(StudioDicCalendar obj) {
		return mapper.insertSelective(obj);
	}

	public List<StudioDicCalendar> selectByCriteriaWithRowbounds(StudioDicCalendarCriteriaWithRowBounds criteriaWithRowBounds) {
		return mapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<StudioDicCalendar> selectByCriteria(StudioDicCalendarCriteria criteria) {
		return mapper.selectByExample(criteria);
	}

	public StudioDicCalendar selectByPrimaryKey(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	public int updateByCriteriaSelective(StudioDicCalendarWithCriteria criteria) {
		return mapper.updateByExampleSelective(criteria.getStudioDicCalendar(), criteria.getCriteria());
	}

	public int updateByCriteria(StudioDicCalendarWithCriteria criteria) {
		return mapper.updateByExample(criteria.getStudioDicCalendar(), criteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(StudioDicCalendar obj) {
		return mapper.updateByPrimaryKeySelective(obj);
	}

	public int updateByPrimaryKey(StudioDicCalendar obj) {
		return mapper.updateByPrimaryKey(obj);
	}



}
