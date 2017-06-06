package com.shangpin.studio.data.mysql.mapper;

import com.shangpin.studio.data.mysql.po.StudioSlotLogistictTrack;
import com.shangpin.studio.data.mysql.po.StudioSlotLogistictTrackCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface StudioSlotLogistictTrackMapper {
    int countByExample(StudioSlotLogistictTrackCriteria example);

    int deleteByExample(StudioSlotLogistictTrackCriteria example);

    int deleteByPrimaryKey(Long studioSlotLogistictTrackId);

    int insert(StudioSlotLogistictTrack record);

    int insertSelective(StudioSlotLogistictTrack record);

    List<StudioSlotLogistictTrack> selectByExampleWithRowbounds(StudioSlotLogistictTrackCriteria example, RowBounds rowBounds);

    List<StudioSlotLogistictTrack> selectByExample(StudioSlotLogistictTrackCriteria example);

    StudioSlotLogistictTrack selectByPrimaryKey(Long studioSlotLogistictTrackId);

    int updateByExampleSelective(@Param("record") StudioSlotLogistictTrack record, @Param("example") StudioSlotLogistictTrackCriteria example);

    int updateByExample(@Param("record") StudioSlotLogistictTrack record, @Param("example") StudioSlotLogistictTrackCriteria example);

    int updateByPrimaryKeySelective(StudioSlotLogistictTrack record);

    int updateByPrimaryKey(StudioSlotLogistictTrack record);
}