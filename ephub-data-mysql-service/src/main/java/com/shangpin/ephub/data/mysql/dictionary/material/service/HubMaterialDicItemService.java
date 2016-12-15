package com.shangpin.ephub.data.mysql.dictionary.material.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.dictionary.material.bean.HubMaterialDicItemCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.dictionary.material.bean.HubMaterialDicItemWithCriteria;
import com.shangpin.ephub.data.mysql.dictionary.material.mapper.HubMaterialDicItemMapper;
import com.shangpin.ephub.data.mysql.dictionary.material.po.HubMaterialDicItem;
import com.shangpin.ephub.data.mysql.dictionary.material.po.HubMaterialDicItemCriteria;

/**
 * <p>Title:HubMaterialDicItemService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:32:33
 */
@Service
public class HubMaterialDicItemService {

	@Autowired
	private HubMaterialDicItemMapper hubMaterialDicItemMapper;

	public int countByCriteria(HubMaterialDicItemCriteria criteria) {
		return hubMaterialDicItemMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubMaterialDicItemCriteria criteria) {
		return hubMaterialDicItemMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long materialDicItemId) {
		return hubMaterialDicItemMapper.deleteByPrimaryKey(materialDicItemId);
	}

	public int insert(HubMaterialDicItem hubMaterialDicItem) {
		return hubMaterialDicItemMapper.insert(hubMaterialDicItem);
	}

	public int insertSelective(HubMaterialDicItem hubMaterialDicItem) {
		return hubMaterialDicItemMapper.insertSelective(hubMaterialDicItem);
	}

	public List<HubMaterialDicItem> selectByCriteriaWithRowbounds(
			HubMaterialDicItemCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubMaterialDicItemMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubMaterialDicItem> selectByCriteria(HubMaterialDicItemCriteria criteria) {
		return hubMaterialDicItemMapper.selectByExample(criteria);
	}

	public HubMaterialDicItem selectByPrimaryKey(Long materialDicItemId) {
		return hubMaterialDicItemMapper.selectByPrimaryKey(materialDicItemId);
	}

	public int updateByCriteriaSelective(HubMaterialDicItemWithCriteria hubMaterialDicItemWithCriteria) {
		return hubMaterialDicItemMapper.updateByExampleSelective(hubMaterialDicItemWithCriteria.getHubMaterialDicItem(), hubMaterialDicItemWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubMaterialDicItemWithCriteria hubMaterialDicItemWithCriteria) {
		return hubMaterialDicItemMapper.updateByExample(hubMaterialDicItemWithCriteria.getHubMaterialDicItem(), hubMaterialDicItemWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubMaterialDicItem hubMaterialDicItem) {
		return hubMaterialDicItemMapper.updateByPrimaryKeySelective(hubMaterialDicItem);
	}

	public int updateByPrimaryKey(HubMaterialDicItem hubMaterialDicItem) {
		return hubMaterialDicItemMapper.updateByPrimaryKey(hubMaterialDicItem);
	}
}
