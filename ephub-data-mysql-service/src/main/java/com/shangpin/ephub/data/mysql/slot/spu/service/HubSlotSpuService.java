package com.shangpin.ephub.data.mysql.slot.spu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.slot.spu.bean.HubSlotSpuCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.slot.spu.bean.HubSlotSpuWithCriteria;
import com.shangpin.ephub.data.mysql.slot.spu.mapper.HubSlotSpuMapper;
import com.shangpin.ephub.data.mysql.slot.spu.po.HubSlotSpu;
import com.shangpin.ephub.data.mysql.slot.spu.po.HubSlotSpuCriteria;

/**
 * <p>Title:HubSlotSpuService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:09:16
 */
@Service
public class HubSlotSpuService {

	@Autowired
	private HubSlotSpuMapper hubSlotSpuMapper;

	public int countByCriteria(HubSlotSpuCriteria criteria) {
		return hubSlotSpuMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSlotSpuCriteria criteria) {
		return hubSlotSpuMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long skuId) {
		return hubSlotSpuMapper.deleteByPrimaryKey(skuId);
	}

	public int insert(HubSlotSpu HubSlotSpu) {
		return hubSlotSpuMapper.insert(HubSlotSpu);
	}

	public int insertSelective(HubSlotSpu HubSlotSpu) {
		return hubSlotSpuMapper.insertSelective(HubSlotSpu);
	}

	public List<HubSlotSpu> selectByCriteriaWithRowbounds(HubSlotSpuCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubSlotSpuMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubSlotSpu> selectByCriteria(HubSlotSpuCriteria criteria) {
		return hubSlotSpuMapper.selectByExample(criteria);
	}

	public HubSlotSpu selectByPrimaryKey(Long skuId) {
		return hubSlotSpuMapper.selectByPrimaryKey(skuId);
	}

	public int updateByCriteriaSelective(HubSlotSpuWithCriteria HubSlotSpuWithCriteria) {
		return hubSlotSpuMapper.updateByExampleSelective(HubSlotSpuWithCriteria.getHubSlotSpu(), HubSlotSpuWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubSlotSpuWithCriteria HubSlotSpuWithCriteria) {
		return hubSlotSpuMapper.updateByExample(HubSlotSpuWithCriteria.getHubSlotSpu(), HubSlotSpuWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSlotSpu HubSlotSpu) {
		return hubSlotSpuMapper.updateByPrimaryKeySelective(HubSlotSpu);
	}

	public int updateByPrimaryKey(HubSlotSpu HubSlotSpu) {
		return hubSlotSpuMapper.updateByPrimaryKey(HubSlotSpu);
	}
}
