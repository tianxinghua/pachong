package com.shangpin.studio.data.mysql.mapper;

import com.shangpin.studio.data.mysql.po.StudioUser;
import com.shangpin.studio.data.mysql.po.StudioUserCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface StudioUserMapper {
    int countByExample(StudioUserCriteria example);

    int deleteByExample(StudioUserCriteria example);

    int deleteByPrimaryKey(Long studioUserId);

    int insert(StudioUser record);

    int insertSelective(StudioUser record);

    List<StudioUser> selectByExampleWithRowbounds(StudioUserCriteria example, RowBounds rowBounds);

    List<StudioUser> selectByExample(StudioUserCriteria example);

    StudioUser selectByPrimaryKey(Long studioUserId);

    int updateByExampleSelective(@Param("record") StudioUser record, @Param("example") StudioUserCriteria example);

    int updateByExample(@Param("record") StudioUser record, @Param("example") StudioUserCriteria example);

    int updateByPrimaryKeySelective(StudioUser record);

    int updateByPrimaryKey(StudioUser record);
}