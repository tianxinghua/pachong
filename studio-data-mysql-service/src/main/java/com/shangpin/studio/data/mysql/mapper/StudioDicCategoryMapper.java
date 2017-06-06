package com.shangpin.studio.data.mysql.mapper;


import java.util.List;

import com.shangpin.studio.data.mysql.po.dic.StudioDicCategory;
import com.shangpin.studio.data.mysql.po.dic.StudioDicCategoryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface StudioDicCategoryMapper {
    int countByExample(StudioDicCategoryCriteria example);

    int deleteByExample(StudioDicCategoryCriteria example);

    int deleteByPrimaryKey(Long studioDicCategoryId);

    int insert(StudioDicCategory record);

    int insertSelective(StudioDicCategory record);

    List<StudioDicCategory> selectByExampleWithRowbounds(StudioDicCategoryCriteria example, RowBounds rowBounds);

    List<StudioDicCategory> selectByExample(StudioDicCategoryCriteria example);

    StudioDicCategory selectByPrimaryKey(Long studioDicCategoryId);

    int updateByExampleSelective(@Param("record") StudioDicCategory record, @Param("example") StudioDicCategoryCriteria example);

    int updateByExample(@Param("record") StudioDicCategory record, @Param("example") StudioDicCategoryCriteria example);

    int updateByPrimaryKeySelective(StudioDicCategory record);

    int updateByPrimaryKey(StudioDicCategory record);
}