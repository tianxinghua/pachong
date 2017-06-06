package com.shangpin.studio.data.mysql.mapper;

import com.shangpin.studio.data.mysql.po.StudioSlotDefectiveSpu;
import com.shangpin.studio.data.mysql.po.StudioSlotDefectiveSpuCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface StudioSlotDefectiveSpuMapper {
    int countByExample(StudioSlotDefectiveSpuCriteria example);

    int deleteByExample(StudioSlotDefectiveSpuCriteria example);

    int deleteByPrimaryKey(Long studioSlotDefectiveSpuId);

    int insert(StudioSlotDefectiveSpu record);

    int insertSelective(StudioSlotDefectiveSpu record);

    List<StudioSlotDefectiveSpu> selectByExampleWithRowbounds(StudioSlotDefectiveSpuCriteria example, RowBounds rowBounds);

    List<StudioSlotDefectiveSpu> selectByExample(StudioSlotDefectiveSpuCriteria example);

    StudioSlotDefectiveSpu selectByPrimaryKey(Long studioSlotDefectiveSpuId);

    int updateByExampleSelective(@Param("record") StudioSlotDefectiveSpu record, @Param("example") StudioSlotDefectiveSpuCriteria example);

    int updateByExample(@Param("record") StudioSlotDefectiveSpu record, @Param("example") StudioSlotDefectiveSpuCriteria example);

    int updateByPrimaryKeySelective(StudioSlotDefectiveSpu record);

    int updateByPrimaryKey(StudioSlotDefectiveSpu record);
}