//package com.shangpin.ephub.data.mysql.slot.dic.category.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.shangpin.ephub.data.mysql.slot.dic.category.bean.HubDicCategoryMeasureTemplateCriteriaWithRowBounds;
//import com.shangpin.ephub.data.mysql.slot.dic.category.bean.HubDicCategoryMeasureTemplateWithCriteria;
//import com.shangpin.ephub.data.mysql.slot.dic.category.po.HubDicCategoryMeasureTemplate;
//import com.shangpin.ephub.data.mysql.slot.dic.category.po.HubDicCategoryMeasureTemplateCriteria;
//import com.shangpin.ephub.data.mysql.slot.dic.category.service.HubDicCategoryMeasureTemplateService;
//
//@RestController
//@RequestMapping("/hub-dic-category-measure-template")
//public class HubDicCategoryMeasureTemplateController {
//	@Autowired
//	private HubDicCategoryMeasureTemplateService hubDicCategoryMeasureTemplateService;
//
//	@RequestMapping(value = "/count-by-criteria")
//    public int countByCriteria(@RequestBody HubDicCategoryMeasureTemplateCriteria criteria){
//    	return hubDicCategoryMeasureTemplateService.countByCriteria(criteria);
//    }
//	@RequestMapping(value = "/delete-by-criteria")
//    public int deleteByCriteria(@RequestBody HubDicCategoryMeasureTemplateCriteria criteria){
//    	return hubDicCategoryMeasureTemplateService.deleteByCriteria(criteria);
//    }
//	@RequestMapping(value = "/delete-by-primary-key")
//    public int deleteByPrimaryKey(Long skuId){
//    	return hubDicCategoryMeasureTemplateService.deleteByPrimaryKey(skuId);
//    }
//	@RequestMapping(value = "/insert")
//    public int insert(@RequestBody HubDicCategoryMeasureTemplate HubDicCategoryMeasureTemplate){
//    	return hubDicCategoryMeasureTemplateService.insert(HubDicCategoryMeasureTemplate);
//    }
//	@RequestMapping(value = "/insert-selective")
//    public int insertSelective(@RequestBody HubDicCategoryMeasureTemplate HubDicCategoryMeasureTemplate){
//    	return hubDicCategoryMeasureTemplateService.insertSelective(HubDicCategoryMeasureTemplate);
//    }
//	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
//    public List<HubDicCategoryMeasureTemplate> selectByCriteriaWithRowbounds(@RequestBody HubDicCategoryMeasureTemplateCriteriaWithRowBounds criteriaWithRowBounds){
//    	return hubDicCategoryMeasureTemplateService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
//    }
//	@RequestMapping(value = "/select-by-criteria")
//    public List<HubDicCategoryMeasureTemplate> selectByCriteria(@RequestBody HubDicCategoryMeasureTemplateCriteria criteria){
//    	return hubDicCategoryMeasureTemplateService.selectByCriteria(criteria);
//    }
//	@RequestMapping(value = "/select-by-primary-key/{skuId}")
//    public HubDicCategoryMeasureTemplate selectByPrimaryKey(@PathVariable(value = "skuId") Long skuId){
//    	return hubDicCategoryMeasureTemplateService.selectByPrimaryKey(skuId);
//    }
//	@RequestMapping(value = "/update-by-criteria-selective")
//    public int updateByCriteriaSelective(@RequestBody HubDicCategoryMeasureTemplateWithCriteria HubDicCategoryMeasureTemplateWithCriteria){
//    	return hubDicCategoryMeasureTemplateService.updateByCriteriaSelective(HubDicCategoryMeasureTemplateWithCriteria);
//    }
//	@RequestMapping(value = "/update-by-criteria")
//    public int updateByCriteria(@RequestBody HubDicCategoryMeasureTemplateWithCriteria HubDicCategoryMeasureTemplateWithCriteria){
//    	return hubDicCategoryMeasureTemplateService.updateByCriteria(HubDicCategoryMeasureTemplateWithCriteria);
//    }
//	@RequestMapping(value = "/update-by-primary-key-selective")
//    public int updateByPrimaryKeySelective(@RequestBody HubDicCategoryMeasureTemplate HubDicCategoryMeasureTemplate){
//    	return hubDicCategoryMeasureTemplateService.updateByPrimaryKeySelective(HubDicCategoryMeasureTemplate);
//    }
//	@RequestMapping(value = "/update-by-primary-key")
//    public int updateByPrimaryKey(@RequestBody HubDicCategoryMeasureTemplate HubDicCategoryMeasureTemplate){
//    	return hubDicCategoryMeasureTemplateService.updateByPrimaryKey(HubDicCategoryMeasureTemplate);
//    }
//}
