package com.shangpin.studio.data.mysql.mapper;


import java.util.List;

import com.shangpin.studio.data.mysql.po.dic.StudioDicCalendar;
import com.shangpin.studio.data.mysql.po.dic.StudioDicCalendarCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface StudioDicCalendarMapper {
    int countByExample(StudioDicCalendarCriteria example);

    int deleteByExample(StudioDicCalendarCriteria example);

    int deleteByPrimaryKey(Long studioDicCalenderId);

    int insert(StudioDicCalendar record);

    int insertSelective(StudioDicCalendar record);

    List<StudioDicCalendar> selectByExampleWithRowbounds(StudioDicCalendarCriteria example, RowBounds rowBounds);

    List<StudioDicCalendar> selectByExample(StudioDicCalendarCriteria example);

    StudioDicCalendar selectByPrimaryKey(Long studioDicCalenderId);

    int updateByExampleSelective(@Param("record") StudioDicCalendar record, @Param("example") StudioDicCalendarCriteria example);

    int updateByExample(@Param("record") StudioDicCalendar record, @Param("example") StudioDicCalendarCriteria example);

    int updateByPrimaryKeySelective(StudioDicCalendar record);

    int updateByPrimaryKey(StudioDicCalendar record);
}