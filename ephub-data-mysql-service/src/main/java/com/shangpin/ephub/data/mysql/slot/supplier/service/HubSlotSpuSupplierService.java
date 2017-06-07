package com.shangpin.ephub.data.mysql.slot.supplier.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.slot.supplier.bean.HubSlotSpuSupplierCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.slot.supplier.bean.HubSlotSpuSupplierWithCriteria;
import com.shangpin.ephub.data.mysql.slot.supplier.mapper.HubSlotSpuSupplierMapper;
import com.shangpin.ephub.data.mysql.slot.supplier.po.HubSlotSpuSupplier;
import com.shangpin.ephub.data.mysql.slot.supplier.po.HubSlotSpuSupplierCriteria;

/**
 * <p>Title:HubSlotSpuSupplierService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:09:16
 */
@Service
public class HubSlotSpuSupplierService {

	@Autowired
	private HubSlotSpuSupplierMapper hubSlotSpuSupplierMapper;

	public int countByCriteria(HubSlotSpuSupplierCriteria criteria) {
		return hubSlotSpuSupplierMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSlotSpuSupplierCriteria criteria) {
		return hubSlotSpuSupplierMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long skuId) {
		return hubSlotSpuSupplierMapper.deleteByPrimaryKey(skuId);
	}

	public int insert(HubSlotSpuSupplier HubSlotSpuSupplier) {
		return hubSlotSpuSupplierMapper.insert(HubSlotSpuSupplier);
	}

	public int insertSelective(HubSlotSpuSupplier HubSlotSpuSupplier) {
		return hubSlotSpuSupplierMapper.insertSelective(HubSlotSpuSupplier);
	}

	public List<HubSlotSpuSupplier> selectByCriteriaWithRowbounds(HubSlotSpuSupplierCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubSlotSpuSupplierMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubSlotSpuSupplier> selectByCriteria(HubSlotSpuSupplierCriteria criteria) {
		return hubSlotSpuSupplierMapper.selectByExample(criteria);
	}

	public HubSlotSpuSupplier selectByPrimaryKey(Long skuId) {
		return hubSlotSpuSupplierMapper.selectByPrimaryKey(skuId);
	}

	public int updateByCriteriaSelective(HubSlotSpuSupplierWithCriteria HubSlotSpuSupplierWithCriteria) {
		return hubSlotSpuSupplierMapper.updateByExampleSelective(HubSlotSpuSupplierWithCriteria.getHubSlotSpuSupplier(), HubSlotSpuSupplierWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubSlotSpuSupplierWithCriteria HubSlotSpuSupplierWithCriteria) {
		return hubSlotSpuSupplierMapper.updateByExample(HubSlotSpuSupplierWithCriteria.getHubSlotSpuSupplier(), HubSlotSpuSupplierWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSlotSpuSupplier HubSlotSpuSupplier) {
		return hubSlotSpuSupplierMapper.updateByPrimaryKeySelective(HubSlotSpuSupplier);
	}

	public int updateByPrimaryKey(HubSlotSpuSupplier HubSlotSpuSupplier) {
		return hubSlotSpuSupplierMapper.updateByPrimaryKey(HubSlotSpuSupplier);
	}
}
