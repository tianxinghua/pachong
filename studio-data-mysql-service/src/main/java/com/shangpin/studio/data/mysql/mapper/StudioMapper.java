package com.shangpin.studio.data.mysql.mapper;

import com.shangpin.studio.data.mysql.po.Studio;
import com.shangpin.studio.data.mysql.po.StudioCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface StudioMapper {
    int countByExample(StudioCriteria example);

    int deleteByExample(StudioCriteria example);

    int deleteByPrimaryKey(Long studioId);

    int insert(Studio record);

    int insertSelective(Studio record);

    List<Studio> selectByExampleWithRowbounds(StudioCriteria example, RowBounds rowBounds);

    List<Studio> selectByExample(StudioCriteria example);

    Studio selectByPrimaryKey(Long studioId);

    int updateByExampleSelective(@Param("record") Studio record, @Param("example") StudioCriteria example);

    int updateByExample(@Param("record") Studio record, @Param("example") StudioCriteria example);

    int updateByPrimaryKeySelective(Studio record);

    int updateByPrimaryKey(Studio record);
}