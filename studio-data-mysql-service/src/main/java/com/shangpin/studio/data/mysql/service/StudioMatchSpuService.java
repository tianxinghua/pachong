package com.shangpin.studio.data.mysql.service;


import com.shangpin.studio.data.mysql.bean.StudioMatchSpuCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioMatchSpuWithCriteria;
import com.shangpin.studio.data.mysql.mapper.StudioMatchSpuMapper;
import com.shangpin.studio.data.mysql.po.StudioMatchSpu;
import com.shangpin.studio.data.mysql.po.StudioMatchSpuCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 StudioMatchSpuService
 */
@Service
@Slf4j
public class StudioMatchSpuService {

	@Autowired
	private StudioMatchSpuMapper mapper;

	public int countByCriteria(StudioMatchSpuCriteria criteria) {
		return mapper.countByExample(criteria);
	}

	public int deleteByCriteria(StudioMatchSpuCriteria criteria) {
		return mapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long skuPendingId) {
		return mapper.deleteByPrimaryKey(skuPendingId);
	}

	public int insert(StudioMatchSpu obj) {
		return mapper.insert(obj);
	}

	public int insertSelective(StudioMatchSpu obj) {
		return mapper.insertSelective(obj);
	}

	public List<StudioMatchSpu> selectByCriteriaWithRowbounds(StudioMatchSpuCriteriaWithRowBounds criteriaWithRowBounds) {
		return mapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<StudioMatchSpu> selectByCriteria(StudioMatchSpuCriteria criteria) {
		return mapper.selectByExample(criteria);
	}

	public StudioMatchSpu selectByPrimaryKey(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	public int updateByCriteriaSelective(StudioMatchSpuWithCriteria criteria) {
		return mapper.updateByExampleSelective(criteria.getStudioMatchSpu(), criteria.getCriteria());
	}

	public int updateByCriteria(StudioMatchSpuWithCriteria criteria) {
		return mapper.updateByExample(criteria.getStudioMatchSpu(), criteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(StudioMatchSpu obj) {
		return mapper.updateByPrimaryKeySelective(obj);
	}

	public int updateByPrimaryKey(StudioMatchSpu obj) {
		return mapper.updateByPrimaryKey(obj);
	}



}
