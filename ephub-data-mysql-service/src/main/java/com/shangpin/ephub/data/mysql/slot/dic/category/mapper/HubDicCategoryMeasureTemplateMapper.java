//package com.shangpin.ephub.data.mysql.slot.dic.category.mapper;
//
//import com.shangpin.ephub.data.mysql.slot.dic.category.po.HubDicCategoryMeasureTemplate;
//import com.shangpin.ephub.data.mysql.slot.dic.category.po.HubDicCategoryMeasureTemplateCriteria;
//import java.util.List;
//
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Param;
//import org.apache.ibatis.session.RowBounds;
//@Mapper
//public interface HubDicCategoryMeasureTemplateMapper {
//    int countByExample(HubDicCategoryMeasureTemplateCriteria example);
//
//    int deleteByExample(HubDicCategoryMeasureTemplateCriteria example);
//
//    int deleteByPrimaryKey(Long categoryMeasureTemplateId);
//
//    int insert(HubDicCategoryMeasureTemplate record);
//
//    int insertSelective(HubDicCategoryMeasureTemplate record);
//
//    List<HubDicCategoryMeasureTemplate> selectByExampleWithRowbounds(HubDicCategoryMeasureTemplateCriteria example, RowBounds rowBounds);
//
//    List<HubDicCategoryMeasureTemplate> selectByExample(HubDicCategoryMeasureTemplateCriteria example);
//
//    HubDicCategoryMeasureTemplate selectByPrimaryKey(Long categoryMeasureTemplateId);
//
//    int updateByExampleSelective(@Param("record") HubDicCategoryMeasureTemplate record, @Param("example") HubDicCategoryMeasureTemplateCriteria example);
//
//    int updateByExample(@Param("record") HubDicCategoryMeasureTemplate record, @Param("example") HubDicCategoryMeasureTemplateCriteria example);
//
//    int updateByPrimaryKeySelective(HubDicCategoryMeasureTemplate record);
//
//    int updateByPrimaryKey(HubDicCategoryMeasureTemplate record);
//}