package com.shangpin.studio.data.mysql.service;


import com.shangpin.studio.data.mysql.bean.StudioSlotLogistictTrackCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioSlotLogistictTrackWithCriteria;
import com.shangpin.studio.data.mysql.mapper.StudioSlotLogistictTrackMapper;
import com.shangpin.studio.data.mysql.po.StudioSlotLogistictTrack;
import com.shangpin.studio.data.mysql.po.StudioSlotLogistictTrackCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
  StudioSlotLogistictTrack
 */
@Service
@Slf4j
public class StudioSlotLogistictTrackService {

	@Autowired
	private StudioSlotLogistictTrackMapper mapper;

	public int countByCriteria(StudioSlotLogistictTrackCriteria criteria) {
		return mapper.countByExample(criteria);
	}

	public int deleteByCriteria(StudioSlotLogistictTrackCriteria criteria) {
		return mapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long id) {
		return mapper.deleteByPrimaryKey(id);
	}

	public int insert(StudioSlotLogistictTrack obj) {
		return mapper.insert(obj);
	}

	public int insertSelective(StudioSlotLogistictTrack obj) {
		return mapper.insertSelective(obj);
	}

	public List<StudioSlotLogistictTrack> selectByCriteriaWithRowbounds(StudioSlotLogistictTrackCriteriaWithRowBounds criteriaWithRowBounds) {
		return mapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<StudioSlotLogistictTrack> selectByCriteria(StudioSlotLogistictTrackCriteria criteria) {
		return mapper.selectByExample(criteria);
	}

	public StudioSlotLogistictTrack selectByPrimaryKey(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	public int updateByCriteriaSelective(StudioSlotLogistictTrackWithCriteria criteria) {
		return mapper.updateByExampleSelective(criteria.getStudioSlotLogistictTrack(), criteria.getCriteria());
	}

	public int updateByCriteria(StudioSlotLogistictTrackWithCriteria criteria) {
		return mapper.updateByExample(criteria.getStudioSlotLogistictTrack(), criteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(StudioSlotLogistictTrack obj) {
		return mapper.updateByPrimaryKeySelective(obj);
	}

	public int updateByPrimaryKey(StudioSlotLogistictTrack obj) {
		return mapper.updateByPrimaryKey(obj);
	}



}
