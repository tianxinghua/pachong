package com.shangpin.studio.data.mysql.service;


import com.shangpin.studio.data.mysql.bean.StudioDicCategoryCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioDicCategoryWithCriteria;
import com.shangpin.studio.data.mysql.mapper.StudioDicCategoryMapper;

import com.shangpin.studio.data.mysql.po.dic.StudioDicCategory;
import com.shangpin.studio.data.mysql.po.dic.StudioDicCategoryCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 StudioDicCategoryService
 */
@Service
@Slf4j
public class StudioDicCategoryService {

	@Autowired
	private StudioDicCategoryMapper mapper;

	public int countByCriteria(StudioDicCategoryCriteria criteria) {
		return mapper.countByExample(criteria);
	}

	public int deleteByCriteria(StudioDicCategoryCriteria criteria) {
		return mapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long skuPendingId) {
		return mapper.deleteByPrimaryKey(skuPendingId);
	}

	public int insert(StudioDicCategory obj) {
		return mapper.insert(obj);
	}

	public int insertSelective(StudioDicCategory obj) {
		return mapper.insertSelective(obj);
	}

	public List<StudioDicCategory> selectByCriteriaWithRowbounds(StudioDicCategoryCriteriaWithRowBounds criteriaWithRowBounds) {
		return mapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<StudioDicCategory> selectByCriteria(StudioDicCategoryCriteria criteria) {
		return mapper.selectByExample(criteria);
	}

	public StudioDicCategory selectByPrimaryKey(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	public int updateByCriteriaSelective(StudioDicCategoryWithCriteria criteria) {
		return mapper.updateByExampleSelective(criteria.getStudioDicCategory(), criteria.getCriteria());
	}

	public int updateByCriteria(StudioDicCategoryWithCriteria criteria) {
		return mapper.updateByExample(criteria.getStudioDicCategory(), criteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(StudioDicCategory obj) {
		return mapper.updateByPrimaryKeySelective(obj);
	}

	public int updateByPrimaryKey(StudioDicCategory obj) {
		return mapper.updateByPrimaryKey(obj);
	}



}
