package com.shangpin.ephub.data.mysql.slot.diccategory.service;

import java.util.List;

import com.shangpin.ephub.data.mysql.slot.diccategory.mapper.HubDicCategoryMeasureTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.slot.diccategory.bean.HubDicCategoryMeasureTemplateCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.slot.diccategory.bean.HubDicCategoryMeasureTemplateWithCriteria;
import com.shangpin.ephub.data.mysql.slot.diccategory.po.HubDicCategoryMeasureTemplate;
import com.shangpin.ephub.data.mysql.slot.diccategory.po.HubDicCategoryMeasureTemplateCriteria;

/**
 * <p>Title:HubDicCategoryMeasureTemplateService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p>
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:09:16
 */
@Service
public class HubDicCategoryMeasureTemplateService {

	@Autowired
	private HubDicCategoryMeasureTemplateMapper hubDicCategoryMeasureTemplateMapper;

	public int countByCriteria(HubDicCategoryMeasureTemplateCriteria criteria) {
		return hubDicCategoryMeasureTemplateMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubDicCategoryMeasureTemplateCriteria criteria) {
		return hubDicCategoryMeasureTemplateMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long skuId) {
		return hubDicCategoryMeasureTemplateMapper.deleteByPrimaryKey(skuId);
	}

	public Long insert(HubDicCategoryMeasureTemplate hubDicCategoryMeasureTemplate) {
		 hubDicCategoryMeasureTemplateMapper.insert(hubDicCategoryMeasureTemplate);
        return hubDicCategoryMeasureTemplate.getCategoryMeasureTemplateId();
	}

	public Long insertSelective(HubDicCategoryMeasureTemplate hubDicCategoryMeasureTemplate) {
		 hubDicCategoryMeasureTemplateMapper.insertSelective(hubDicCategoryMeasureTemplate);
        return hubDicCategoryMeasureTemplate.getCategoryMeasureTemplateId();
	}

	public List<HubDicCategoryMeasureTemplate> selectByCriteriaWithRowbounds(HubDicCategoryMeasureTemplateCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubDicCategoryMeasureTemplateMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubDicCategoryMeasureTemplate> selectByCriteria(HubDicCategoryMeasureTemplateCriteria criteria) {
		return hubDicCategoryMeasureTemplateMapper.selectByExample(criteria);
	}

	public HubDicCategoryMeasureTemplate selectByPrimaryKey(Long skuId) {
		return hubDicCategoryMeasureTemplateMapper.selectByPrimaryKey(skuId);
	}

	public int updateByCriteriaSelective(HubDicCategoryMeasureTemplateWithCriteria HubDicCategoryMeasureTemplateWithCriteria) {
		return hubDicCategoryMeasureTemplateMapper.updateByExampleSelective(HubDicCategoryMeasureTemplateWithCriteria.getHubDicCategoryMeasureTemplate(), HubDicCategoryMeasureTemplateWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubDicCategoryMeasureTemplateWithCriteria HubDicCategoryMeasureTemplateWithCriteria) {
		return hubDicCategoryMeasureTemplateMapper.updateByExample(HubDicCategoryMeasureTemplateWithCriteria.getHubDicCategoryMeasureTemplate(), HubDicCategoryMeasureTemplateWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubDicCategoryMeasureTemplate HubDicCategoryMeasureTemplate) {
		return hubDicCategoryMeasureTemplateMapper.updateByPrimaryKeySelective(HubDicCategoryMeasureTemplate);
	}

	public int updateByPrimaryKey(HubDicCategoryMeasureTemplate HubDicCategoryMeasureTemplate) {
		return hubDicCategoryMeasureTemplateMapper.updateByPrimaryKey(HubDicCategoryMeasureTemplate);
	}
}
