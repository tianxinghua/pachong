package com.shangpin.ephub.data.mysql.slot.pic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.slot.pic.bean.HubSlotSpuPicCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.slot.pic.bean.HubSlotSpuPicWithCriteria;
import com.shangpin.ephub.data.mysql.slot.pic.mapper.HubSlotSpuPicMapper;
import com.shangpin.ephub.data.mysql.slot.pic.po.HubSlotSpuPic;
import com.shangpin.ephub.data.mysql.slot.pic.po.HubSlotSpuPicCriteria;

/**
 * <p>Title:HubSlotSpuPicService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:09:16
 */
@Service
public class HubSlotSpuPicService {

	@Autowired
	private HubSlotSpuPicMapper hubSlotSpuPicMapper;

	public int countByCriteria(HubSlotSpuPicCriteria criteria) {
		return hubSlotSpuPicMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSlotSpuPicCriteria criteria) {
		return hubSlotSpuPicMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long skuId) {
		return hubSlotSpuPicMapper.deleteByPrimaryKey(skuId);
	}

	public int  insert(HubSlotSpuPic HubSlotSpuPic) {
		return hubSlotSpuPicMapper.insert(HubSlotSpuPic);
	}

	public int  insertSelective(HubSlotSpuPic HubSlotSpuPic) {
		return hubSlotSpuPicMapper.insertSelective(HubSlotSpuPic);
	}

	public List<HubSlotSpuPic> selectByCriteriaWithRowbounds(HubSlotSpuPicCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubSlotSpuPicMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubSlotSpuPic> selectByCriteria(HubSlotSpuPicCriteria criteria) {
		return hubSlotSpuPicMapper.selectByExample(criteria);
	}

	public HubSlotSpuPic selectByPrimaryKey(Long skuId) {
		return hubSlotSpuPicMapper.selectByPrimaryKey(skuId);
	}

	public int updateByCriteriaSelective(HubSlotSpuPicWithCriteria HubSlotSpuPicWithCriteria) {
		return hubSlotSpuPicMapper.updateByExampleSelective(HubSlotSpuPicWithCriteria.getHubSlotSpuPic(), HubSlotSpuPicWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubSlotSpuPicWithCriteria HubSlotSpuPicWithCriteria) {
		return hubSlotSpuPicMapper.updateByExample(HubSlotSpuPicWithCriteria.getHubSlotSpuPic(), HubSlotSpuPicWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSlotSpuPic HubSlotSpuPic) {
		return hubSlotSpuPicMapper.updateByPrimaryKeySelective(HubSlotSpuPic);
	}

	public int updateByPrimaryKey(HubSlotSpuPic HubSlotSpuPic) {
		return hubSlotSpuPicMapper.updateByPrimaryKey(HubSlotSpuPic);
	}
}
