package com.shangpin.studio.data.mysql.service;


import com.shangpin.studio.data.mysql.bean.StudioCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioWithCriteria;
import com.shangpin.studio.data.mysql.mapper.StudioMapper;
import com.shangpin.studio.data.mysql.po.Studio;
import com.shangpin.studio.data.mysql.po.StudioCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
  studio 服务
 */
@Service
@Slf4j
public class StudioService {

	@Autowired
	private StudioMapper studioMapper;

	public int countByCriteria(StudioCriteria criteria) {
		return studioMapper.countByExample(criteria);
	}

	public int deleteByCriteria(StudioCriteria criteria) {
		return studioMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long skuPendingId) {
		return studioMapper.deleteByPrimaryKey(skuPendingId);
	}

	public int insert(Studio studio) {
		return studioMapper.insert(studio);
	}

	public int insertSelective(Studio studio) {
		return studioMapper.insertSelective(studio);
	}

	public List<Studio> selectByCriteriaWithRowbounds(StudioCriteriaWithRowBounds criteriaWithRowBounds) {
		return studioMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<Studio> selectByCriteria(StudioCriteria criteria) {
		return studioMapper.selectByExample(criteria);
	}

	public Studio selectByPrimaryKey(Long studioId) {
		return studioMapper.selectByPrimaryKey(studioId);
	}

	public int updateByCriteriaSelective(StudioWithCriteria StudioWithCriteria) {
		return studioMapper.updateByExampleSelective(StudioWithCriteria.getStudio(), StudioWithCriteria.getCriteria());
	}

	public int updateByCriteria(StudioWithCriteria StudioWithCriteria) {
		return studioMapper.updateByExample(StudioWithCriteria.getStudio(), StudioWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(Studio studio) {
		return studioMapper.updateByPrimaryKeySelective(studio);
	}

	public int updateByPrimaryKey(Studio studio) {
		return studioMapper.updateByPrimaryKey(studio);
	}



}
