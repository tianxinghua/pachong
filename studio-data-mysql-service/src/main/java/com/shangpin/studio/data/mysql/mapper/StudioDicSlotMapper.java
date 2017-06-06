package com.shangpin.studio.data.mysql.mapper;


import java.util.List;

import com.shangpin.studio.data.mysql.po.dic.StudioDicSlot;
import com.shangpin.studio.data.mysql.po.dic.StudioDicSlotCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

@Mapper
public interface StudioDicSlotMapper {
    int countByExample(StudioDicSlotCriteria example);

    int deleteByExample(StudioDicSlotCriteria example);

    int deleteByPrimaryKey(Long studioDicSlotId);

    int insert(StudioDicSlot record);

    int insertSelective(StudioDicSlot record);

    List<StudioDicSlot> selectByExampleWithRowbounds(StudioDicSlotCriteria example, RowBounds rowBounds);

    List<StudioDicSlot> selectByExample(StudioDicSlotCriteria example);

    StudioDicSlot selectByPrimaryKey(Long studioDicSlotId);

    int updateByExampleSelective(@Param("record") StudioDicSlot record, @Param("example") StudioDicSlotCriteria example);

    int updateByExample(@Param("record") StudioDicSlot record, @Param("example") StudioDicSlotCriteria example);

    int updateByPrimaryKeySelective(StudioDicSlot record);

    int updateByPrimaryKey(StudioDicSlot record);
}