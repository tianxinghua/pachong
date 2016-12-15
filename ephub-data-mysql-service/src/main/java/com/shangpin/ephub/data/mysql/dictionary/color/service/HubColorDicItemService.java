package com.shangpin.ephub.data.mysql.dictionary.color.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.dictionary.color.bean.HubColorDicItemCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.dictionary.color.bean.HubColorDicItemWithCriteria;
import com.shangpin.ephub.data.mysql.dictionary.color.mapper.HubColorDicItemMapper;
import com.shangpin.ephub.data.mysql.dictionary.color.po.HubColorDicItem;
import com.shangpin.ephub.data.mysql.dictionary.color.po.HubColorDicItemCriteria;

/**
 * <p>Title:HubColorDicItemService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:27:29
 */
@Service
public class HubColorDicItemService {
	
	@Autowired
	private HubColorDicItemMapper hubColorDicItemMapper;

	public int countByCriteria(HubColorDicItemCriteria criteria) {
		return hubColorDicItemMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubColorDicItemCriteria criteria) {
		return hubColorDicItemMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long colorDicItemId) {
		return hubColorDicItemMapper.deleteByPrimaryKey(colorDicItemId);
	}

	public int insert(HubColorDicItem hubColorDicItem) {
		return hubColorDicItemMapper.insert(hubColorDicItem);
	}

	public int insertSelective(HubColorDicItem hubColorDicItem) {
		return hubColorDicItemMapper.insertSelective(hubColorDicItem);
	}

	public List<HubColorDicItem> selectByCriteriaWithRowbounds(
			HubColorDicItemCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubColorDicItemMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubColorDicItem> selectByCriteria(HubColorDicItemCriteria criteria) {
		return hubColorDicItemMapper.selectByExample(criteria);
	}

	public HubColorDicItem selectByPrimaryKey(Long colorDicItemId) {
		return hubColorDicItemMapper.selectByPrimaryKey(colorDicItemId);
	}

	public int updateByCriteriaSelective(HubColorDicItemWithCriteria hubColorDicItemWithCriteria) {
		return hubColorDicItemMapper.updateByExampleSelective(hubColorDicItemWithCriteria.getHubColorDicItem(), hubColorDicItemWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubColorDicItemWithCriteria hubColorDicItemWithCriteria) {
		return hubColorDicItemMapper.updateByExample(hubColorDicItemWithCriteria.getHubColorDicItem(), hubColorDicItemWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubColorDicItem hubColorDicItem) {
		return hubColorDicItemMapper.updateByPrimaryKeySelective(hubColorDicItem);
	}

	public int updateByPrimaryKey(HubColorDicItem hubColorDicItem) {
		return hubColorDicItemMapper.updateByPrimaryKey(hubColorDicItem);
	}

}
