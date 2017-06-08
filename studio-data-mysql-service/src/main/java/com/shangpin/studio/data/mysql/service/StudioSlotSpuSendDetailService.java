package com.shangpin.studio.data.mysql.service;


import com.shangpin.studio.data.mysql.bean.StudioSlotSpuSendDetailCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioSlotSpuSendDetailWithCriteria;
import com.shangpin.studio.data.mysql.mapper.StudioSlotMapper;
import com.shangpin.studio.data.mysql.mapper.StudioSlotSpuSendDetailMapper;
import com.shangpin.studio.data.mysql.po.StudioSlotSpuSendDetail;
import com.shangpin.studio.data.mysql.po.StudioSlotSpuSendDetailCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
   studioSlotSpuSendDetail 服务
 */
@Service
@Slf4j
public class StudioSlotSpuSendDetailService {

	@Autowired
	private StudioSlotSpuSendDetailMapper mapper;

	public int countByCriteria(StudioSlotSpuSendDetailCriteria criteria) {
		return mapper.countByExample(criteria);
	}

	public int deleteByCriteria(StudioSlotSpuSendDetailCriteria criteria) {
		return mapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long id) {
		return mapper.deleteByPrimaryKey(id);
	}

	public int insert(StudioSlotSpuSendDetail obj) {
		return mapper.insert(obj);
	}

	public int insertSelective(StudioSlotSpuSendDetail obj) {
		return mapper.insertSelective(obj);
	}

	public List<StudioSlotSpuSendDetail> selectByCriteriaWithRowbounds(StudioSlotSpuSendDetailCriteriaWithRowBounds criteriaWithRowBounds) {
		return mapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<StudioSlotSpuSendDetail> selectByCriteria(StudioSlotSpuSendDetailCriteria criteria) {
		return mapper.selectByExample(criteria);
	}

	public StudioSlotSpuSendDetail selectByPrimaryKey(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	public int updateByCriteriaSelective(StudioSlotSpuSendDetailWithCriteria criteria) {
		return mapper.updateByExampleSelective(criteria.getStudioSlotSpuSendDetail(), criteria.getCriteria());
	}

	public int updateByCriteria(StudioSlotSpuSendDetailWithCriteria criteria) {
		return mapper.updateByExample(criteria.getStudioSlotSpuSendDetail(), criteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(StudioSlotSpuSendDetail obj) {
		return mapper.updateByPrimaryKeySelective(obj);
	}

	public int updateByPrimaryKey(StudioSlotSpuSendDetail obj) {
		return mapper.updateByPrimaryKey(obj);
	}



}
