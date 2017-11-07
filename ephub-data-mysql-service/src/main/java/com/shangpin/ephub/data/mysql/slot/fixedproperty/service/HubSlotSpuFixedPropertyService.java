package com.shangpin.ephub.data.mysql.slot.fixedproperty.service;



import com.shangpin.ephub.data.mysql.slot.fixedproperty.bean.HubSlotSpuFixedPropertyCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.slot.fixedproperty.bean.HubSlotSpuFixedPropertyWithCriteria;
import com.shangpin.ephub.data.mysql.slot.fixedproperty.mapper.HubSlotSpuFixedPropertyMapper;
import com.shangpin.ephub.data.mysql.slot.fixedproperty.po.HubSlotSpuFixedProperty;
import com.shangpin.ephub.data.mysql.slot.fixedproperty.po.HubSlotSpuFixedPropertyCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**

 */
@Service
@Slf4j
public class HubSlotSpuFixedPropertyService {

	@Autowired
	private HubSlotSpuFixedPropertyMapper mapper;

	public int countByCriteria(HubSlotSpuFixedPropertyCriteria criteria) {
		return mapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSlotSpuFixedPropertyCriteria criteria) {
		return mapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long skuPendingId) {
		return mapper.deleteByPrimaryKey(skuPendingId);
	}

	public int insert(HubSlotSpuFixedProperty obj) {
		return mapper.insert(obj);
	}

	public int insertSelective(HubSlotSpuFixedProperty obj) {
		return mapper.insertSelective(obj);
	}

	public List<HubSlotSpuFixedProperty> selectByCriteriaWithRowbounds(HubSlotSpuFixedPropertyCriteriaWithRowBounds criteriaWithRowBounds) {
		return mapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubSlotSpuFixedProperty> selectByCriteria(HubSlotSpuFixedPropertyCriteria criteria) {
		return mapper.selectByExample(criteria);
	}

	public HubSlotSpuFixedProperty selectByPrimaryKey(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	public int updateByCriteriaSelective(HubSlotSpuFixedPropertyWithCriteria criteria) {
		return mapper.updateByExampleSelective(criteria.getHubSlotSpuFixedProperty(), criteria.getCriteria());
	}

	public int updateByCriteria(HubSlotSpuFixedPropertyWithCriteria criteria) {
		return mapper.updateByExample(criteria.getHubSlotSpuFixedProperty(), criteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSlotSpuFixedProperty obj) {
		return mapper.updateByPrimaryKeySelective(obj);
	}

	public int updateByPrimaryKey(HubSlotSpuFixedProperty obj) {
		return mapper.updateByPrimaryKey(obj);
	}



}
