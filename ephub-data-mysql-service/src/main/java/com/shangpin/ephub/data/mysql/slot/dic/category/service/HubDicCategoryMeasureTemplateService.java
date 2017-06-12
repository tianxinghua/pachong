//package com.shangpin.ephub.data.mysql.slot.dic.category.service;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.shangpin.ephub.data.mysql.slot.dic.category.bean.HubDicCategoryMeasureTemplateCriteriaWithRowBounds;
//import com.shangpin.ephub.data.mysql.slot.dic.category.bean.HubDicCategoryMeasureTemplateWithCriteria;
//import com.shangpin.ephub.data.mysql.slot.dic.category.mapper.HubDicCategoryMeasureTemplateMapper;
//import com.shangpin.ephub.data.mysql.slot.dic.category.po.HubDicCategoryMeasureTemplate;
//import com.shangpin.ephub.data.mysql.slot.dic.category.po.HubDicCategoryMeasureTemplateCriteria;
//
///**
// * <p>Title:HubDicCategoryMeasureTemplateService.java </p>
// * <p>Description: </p>
// * <p>Company: www.shangpin.com</p> 
// * @author yanxiaobin
// * @date 2016年12月14日 下午5:09:16
// */
//@Service
//public class HubDicCategoryMeasureTemplateService {
//
//	@Autowired
//	private HubDicCategoryMeasureTemplateMapper hubDicCategoryMeasureTemplateMapper;
//
//	public int countByCriteria(HubDicCategoryMeasureTemplateCriteria criteria) {
//		return hubDicCategoryMeasureTemplateMapper.countByExample(criteria);
//	}
//
//	public int deleteByCriteria(HubDicCategoryMeasureTemplateCriteria criteria) {
//		return hubDicCategoryMeasureTemplateMapper.deleteByExample(criteria);
//	}
//
//	public int deleteByPrimaryKey(Long skuId) {
//		return hubDicCategoryMeasureTemplateMapper.deleteByPrimaryKey(skuId);
//	}
//
//	public int insert(HubDicCategoryMeasureTemplate HubDicCategoryMeasureTemplate) {
//		return hubDicCategoryMeasureTemplateMapper.insert(HubDicCategoryMeasureTemplate);
//	}
//
//	public int insertSelective(HubDicCategoryMeasureTemplate HubDicCategoryMeasureTemplate) {
//		return hubDicCategoryMeasureTemplateMapper.insertSelective(HubDicCategoryMeasureTemplate);
//	}
//
//	public List<HubDicCategoryMeasureTemplate> selectByCriteriaWithRowbounds(HubDicCategoryMeasureTemplateCriteriaWithRowBounds criteriaWithRowBounds) {
//		return hubDicCategoryMeasureTemplateMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
//	}
//
//	public List<HubDicCategoryMeasureTemplate> selectByCriteria(HubDicCategoryMeasureTemplateCriteria criteria) {
//		return hubDicCategoryMeasureTemplateMapper.selectByExample(criteria);
//	}
//
//	public HubDicCategoryMeasureTemplate selectByPrimaryKey(Long skuId) {
//		return hubDicCategoryMeasureTemplateMapper.selectByPrimaryKey(skuId);
//	}
//
//	public int updateByCriteriaSelective(HubDicCategoryMeasureTemplateWithCriteria HubDicCategoryMeasureTemplateWithCriteria) {
//		return hubDicCategoryMeasureTemplateMapper.updateByExampleSelective(HubDicCategoryMeasureTemplateWithCriteria.getHubDicCategoryMeasureTemplate(), HubDicCategoryMeasureTemplateWithCriteria.getCriteria());
//	}
//
//	public int updateByCriteria(HubDicCategoryMeasureTemplateWithCriteria HubDicCategoryMeasureTemplateWithCriteria) {
//		return hubDicCategoryMeasureTemplateMapper.updateByExample(HubDicCategoryMeasureTemplateWithCriteria.getHubDicCategoryMeasureTemplate(), HubDicCategoryMeasureTemplateWithCriteria.getCriteria());
//	}
//
//	public int updateByPrimaryKeySelective(HubDicCategoryMeasureTemplate HubDicCategoryMeasureTemplate) {
//		return hubDicCategoryMeasureTemplateMapper.updateByPrimaryKeySelective(HubDicCategoryMeasureTemplate);
//	}
//
//	public int updateByPrimaryKey(HubDicCategoryMeasureTemplate HubDicCategoryMeasureTemplate) {
//		return hubDicCategoryMeasureTemplateMapper.updateByPrimaryKey(HubDicCategoryMeasureTemplate);
//	}
//}
