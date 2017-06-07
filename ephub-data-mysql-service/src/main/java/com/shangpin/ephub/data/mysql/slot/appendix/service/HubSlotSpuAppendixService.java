package com.shangpin.ephub.data.mysql.slot.appendix.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.slot.appendix.bean.HubSlotSpuAppendixCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.slot.appendix.bean.HubSlotSpuAppendixWithCriteria;
import com.shangpin.ephub.data.mysql.slot.appendix.mapper.HubSlotSpuAppendixMapper;
import com.shangpin.ephub.data.mysql.slot.appendix.po.HubSlotSpuAppendix;
import com.shangpin.ephub.data.mysql.slot.appendix.po.HubSlotSpuAppendixCriteria;

/**
 * <p>Title:HubSlotSpuAppendixService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:09:16
 */
@Service
public class HubSlotSpuAppendixService {

	@Autowired
	private HubSlotSpuAppendixMapper hubSlotSpuAppendixMapper;

	public int countByCriteria(HubSlotSpuAppendixCriteria criteria) {
		return hubSlotSpuAppendixMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSlotSpuAppendixCriteria criteria) {
		return hubSlotSpuAppendixMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long skuId) {
		return hubSlotSpuAppendixMapper.deleteByPrimaryKey(skuId);
	}

	public int insert(HubSlotSpuAppendix HubSlotSpuAppendix) {
		return hubSlotSpuAppendixMapper.insert(HubSlotSpuAppendix);
	}

	public int insertSelective(HubSlotSpuAppendix HubSlotSpuAppendix) {
		return hubSlotSpuAppendixMapper.insertSelective(HubSlotSpuAppendix);
	}

	public List<HubSlotSpuAppendix> selectByCriteriaWithRowbounds(HubSlotSpuAppendixCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubSlotSpuAppendixMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubSlotSpuAppendix> selectByCriteria(HubSlotSpuAppendixCriteria criteria) {
		return hubSlotSpuAppendixMapper.selectByExample(criteria);
	}

	public HubSlotSpuAppendix selectByPrimaryKey(Long skuId) {
		return hubSlotSpuAppendixMapper.selectByPrimaryKey(skuId);
	}

	public int updateByCriteriaSelective(HubSlotSpuAppendixWithCriteria HubSlotSpuAppendixWithCriteria) {
		return hubSlotSpuAppendixMapper.updateByExampleSelective(HubSlotSpuAppendixWithCriteria.getHubSlotSpuAppendix(), HubSlotSpuAppendixWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubSlotSpuAppendixWithCriteria HubSlotSpuAppendixWithCriteria) {
		return hubSlotSpuAppendixMapper.updateByExample(HubSlotSpuAppendixWithCriteria.getHubSlotSpuAppendix(), HubSlotSpuAppendixWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSlotSpuAppendix HubSlotSpuAppendix) {
		return hubSlotSpuAppendixMapper.updateByPrimaryKeySelective(HubSlotSpuAppendix);
	}

	public int updateByPrimaryKey(HubSlotSpuAppendix HubSlotSpuAppendix) {
		return hubSlotSpuAppendixMapper.updateByPrimaryKey(HubSlotSpuAppendix);
	}
}
