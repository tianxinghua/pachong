package com.shangpin.studio.data.mysql.service;


import com.shangpin.studio.data.mysql.bean.StudioSlotReturnMasterCriteriaWithRowBounds;
import com.shangpin.studio.data.mysql.bean.StudioSlotReturnMasterWithCriteria;
import com.shangpin.studio.data.mysql.mapper.StudioSlotReturnMasterMapper;
import com.shangpin.studio.data.mysql.mapper.StudioSlotSpuSendDetailMapper;
import com.shangpin.studio.data.mysql.po.StudioSlotReturnMaster;
import com.shangpin.studio.data.mysql.po.StudioSlotReturnMasterCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
   studioSlotSpuSendDetail 服务
 */
@Service
@Slf4j
public class StudioSlotReturnMasterService {

	@Autowired
	private StudioSlotReturnMasterMapper mapper;

	public int countByCriteria(StudioSlotReturnMasterCriteria criteria) {
		return mapper.countByExample(criteria);
	}

	public int deleteByCriteria(StudioSlotReturnMasterCriteria criteria) {
		return mapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long id) {
		return mapper.deleteByPrimaryKey(id);
	}

	public int insert(StudioSlotReturnMaster obj) {
		return mapper.insert(obj);
	}

	public int insertSelective(StudioSlotReturnMaster obj) {
		return mapper.insertSelective(obj);
	}

	public List<StudioSlotReturnMaster> selectByCriteriaWithRowbounds(StudioSlotReturnMasterCriteriaWithRowBounds criteriaWithRowBounds) {
		return mapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<StudioSlotReturnMaster> selectByCriteria(StudioSlotReturnMasterCriteria criteria) {
		return mapper.selectByExample(criteria);
	}

	public StudioSlotReturnMaster selectByPrimaryKey(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	public int updateByCriteriaSelective(StudioSlotReturnMasterWithCriteria criteria) {
		return mapper.updateByExampleSelective(criteria.getStudioSlotReturnMaster(), criteria.getCriteria());
	}

	public int updateByCriteria(StudioSlotReturnMasterWithCriteria criteria) {
		return mapper.updateByExample(criteria.getStudioSlotReturnMaster(), criteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(StudioSlotReturnMaster obj) {
		return mapper.updateByPrimaryKeySelective(obj);
	}

	public int updateByPrimaryKey(StudioSlotReturnMaster obj) {
		return mapper.updateByPrimaryKey(obj);
	}



}
