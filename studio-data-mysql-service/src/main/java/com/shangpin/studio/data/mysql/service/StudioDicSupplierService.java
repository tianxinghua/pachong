package com.shangpin.studio.data.mysql.service;


import com.shangpin.studio.data.mysql.bean.StudioDicSupplierCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioDicSupplierWithCriteria;
import com.shangpin.studio.data.mysql.mapper.StudioDicSupplierMapper;

import com.shangpin.studio.data.mysql.po.dic.StudioDicSupplier;
import com.shangpin.studio.data.mysql.po.dic.StudioDicSupplierCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
  StudioDicSupplier ����
 */
@Service
@Slf4j
public class StudioDicSupplierService {

	@Autowired
	private StudioDicSupplierMapper mapper;

	public int countByCriteria(StudioDicSupplierCriteria criteria) {
		return mapper.countByExample(criteria);
	}

	public int deleteByCriteria(StudioDicSupplierCriteria criteria) {
		return mapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long skuPendingId) {
		return mapper.deleteByPrimaryKey(skuPendingId);
	}

	public int insert(StudioDicSupplier obj) {
		return mapper.insert(obj);
	}

	public int insertSelective(StudioDicSupplier obj) {
		return mapper.insertSelective(obj);
	}

	public List<StudioDicSupplier> selectByCriteriaWithRowbounds(StudioDicSupplierCriteriaWithRowBounds criteriaWithRowBounds) {
		return mapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<StudioDicSupplier> selectByCriteria(StudioDicSupplierCriteria criteria) {
		return mapper.selectByExample(criteria);
	}

	public StudioDicSupplier selectByPrimaryKey(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	public int updateByCriteriaSelective(StudioDicSupplierWithCriteria criteria) {
		return mapper.updateByExampleSelective(criteria.getStudioDicSupplier(), criteria.getCriteria());
	}

	public int updateByCriteria(StudioDicSupplierWithCriteria criteria) {
		return mapper.updateByExample(criteria.getStudioDicSupplier(), criteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(StudioDicSupplier obj) {
		return mapper.updateByPrimaryKeySelective(obj);
	}

	public int updateByPrimaryKey(StudioDicSupplier obj) {
		return mapper.updateByPrimaryKey(obj);
	}



}
