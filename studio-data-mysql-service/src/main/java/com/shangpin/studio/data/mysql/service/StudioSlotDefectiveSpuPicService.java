package com.shangpin.studio.data.mysql.service;


import com.shangpin.studio.data.mysql.bean.StudioSlotDefectiveSpuPicCriteriaWithRowBounds;

import com.shangpin.studio.data.mysql.bean.StudioSlotDefectiveSpuPicWithCriteria;
import com.shangpin.studio.data.mysql.mapper.StudioSlotDefectiveSpuPicMapper;
import com.shangpin.studio.data.mysql.po.StudioSlotDefectiveSpuPic;
import com.shangpin.studio.data.mysql.po.StudioSlotDefectiveSpuPicCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
  StudioSlotDefectiveSpuPic
 */
@Service
@Slf4j
public class StudioSlotDefectiveSpuPicService {

	@Autowired
	private StudioSlotDefectiveSpuPicMapper mapper;

	public int countByCriteria(StudioSlotDefectiveSpuPicCriteria criteria) {
		return mapper.countByExample(criteria);
	}

	public int deleteByCriteria(StudioSlotDefectiveSpuPicCriteria criteria) {
		return mapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long skuPendingId) {
		return mapper.deleteByPrimaryKey(skuPendingId);
	}

	public int insert(StudioSlotDefectiveSpuPic obj) {
		return mapper.insert(obj);
	}

	public int insertSelective(StudioSlotDefectiveSpuPic obj) {
		return mapper.insertSelective(obj);
	}

	public List<StudioSlotDefectiveSpuPic> selectByCriteriaWithRowbounds(StudioSlotDefectiveSpuPicCriteriaWithRowBounds criteriaWithRowBounds) {
		return mapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<StudioSlotDefectiveSpuPic> selectByCriteria(StudioSlotDefectiveSpuPicCriteria criteria) {
		return mapper.selectByExample(criteria);
	}

	public StudioSlotDefectiveSpuPic selectByPrimaryKey(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	public int updateByCriteriaSelective(StudioSlotDefectiveSpuPicWithCriteria criteria) {
		return mapper.updateByExampleSelective(criteria.getStudioSlotDefectiveSpuPic(), criteria.getCriteria());
	}

	public int updateByCriteria(StudioSlotDefectiveSpuPicWithCriteria criteria) {
		return mapper.updateByExample(criteria.getStudioSlotDefectiveSpuPic(), criteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(StudioSlotDefectiveSpuPic obj) {
		return mapper.updateByPrimaryKeySelective(obj);
	}

	public int updateByPrimaryKey(StudioSlotDefectiveSpuPic obj) {
		return mapper.updateByPrimaryKey(obj);
	}



}
