package com.shangpin.studio.data.mysql.service;


import com.shangpin.studio.data.mysql.bean.StudioSlotDefectiveSpuCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioSlotDefectiveSpuWithCriteria;
import com.shangpin.studio.data.mysql.mapper.StudioSlotDefectiveSpuMapper;
import com.shangpin.studio.data.mysql.po.StudioSlotDefectiveSpu;
import com.shangpin.studio.data.mysql.po.StudioSlotDefectiveSpuCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 StudioSlotDefectiveSpuService
 */
@Service
@Slf4j
public class StudioSlotDefectiveSpuService {

	@Autowired
	private StudioSlotDefectiveSpuMapper mapper;

	public int countByCriteria(StudioSlotDefectiveSpuCriteria criteria) {
		return mapper.countByExample(criteria);
	}

	public int deleteByCriteria(StudioSlotDefectiveSpuCriteria criteria) {
		return mapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long skuPendingId) {
		return mapper.deleteByPrimaryKey(skuPendingId);
	}

	public int insert(StudioSlotDefectiveSpu obj) {
		return mapper.insert(obj);
	}

	public int insertSelective(StudioSlotDefectiveSpu obj) {
		return mapper.insertSelective(obj);
	}

	public List<StudioSlotDefectiveSpu> selectByCriteriaWithRowbounds(StudioSlotDefectiveSpuCriteriaWithRowBounds criteriaWithRowBounds) {
		return mapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<StudioSlotDefectiveSpu> selectByCriteria(StudioSlotDefectiveSpuCriteria criteria) {
		return mapper.selectByExample(criteria);
	}

	public StudioSlotDefectiveSpu selectByPrimaryKey(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	public int updateByCriteriaSelective(StudioSlotDefectiveSpuWithCriteria criteria) {
		return mapper.updateByExampleSelective(criteria.getStudioSlotDefectiveSpu(), criteria.getCriteria());
	}

	public int updateByCriteria(StudioSlotDefectiveSpuWithCriteria criteria) {
		return mapper.updateByExample(criteria.getStudioSlotDefectiveSpu(), criteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(StudioSlotDefectiveSpu obj) {
		return mapper.updateByPrimaryKeySelective(obj);
	}

	public int updateByPrimaryKey(StudioSlotDefectiveSpu obj) {
		return mapper.updateByPrimaryKey(obj);
	}



}
