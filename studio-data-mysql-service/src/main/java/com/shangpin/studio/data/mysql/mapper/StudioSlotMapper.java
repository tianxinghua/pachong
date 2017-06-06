package com.shangpin.studio.data.mysql.mapper;

import com.shangpin.studio.data.mysql.po.StudioSlot;
import com.shangpin.studio.data.mysql.po.StudioSlotCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface StudioSlotMapper {
    int countByExample(StudioSlotCriteria example);

    int deleteByExample(StudioSlotCriteria example);

    int deleteByPrimaryKey(Long studioSlotId);

    int insert(StudioSlot record);

    int insertSelective(StudioSlot record);

    List<StudioSlot> selectByExampleWithRowbounds(StudioSlotCriteria example, RowBounds rowBounds);

    List<StudioSlot> selectByExample(StudioSlotCriteria example);

    StudioSlot selectByPrimaryKey(Long studioSlotId);

    int updateByExampleSelective(@Param("record") StudioSlot record, @Param("example") StudioSlotCriteria example);

    int updateByExample(@Param("record") StudioSlot record, @Param("example") StudioSlotCriteria example);

    int updateByPrimaryKeySelective(StudioSlot record);

    int updateByPrimaryKey(StudioSlot record);
}