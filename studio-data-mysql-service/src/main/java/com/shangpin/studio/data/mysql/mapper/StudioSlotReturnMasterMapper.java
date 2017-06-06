package com.shangpin.studio.data.mysql.mapper;

import com.shangpin.studio.data.mysql.po.StudioSlotReturnMaster;
import com.shangpin.studio.data.mysql.po.StudioSlotReturnMasterCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface StudioSlotReturnMasterMapper {
    int countByExample(StudioSlotReturnMasterCriteria example);

    int deleteByExample(StudioSlotReturnMasterCriteria example);

    int deleteByPrimaryKey(Long studioSlotReturnMasterId);

    int insert(StudioSlotReturnMaster record);

    int insertSelective(StudioSlotReturnMaster record);

    List<StudioSlotReturnMaster> selectByExampleWithRowbounds(StudioSlotReturnMasterCriteria example, RowBounds rowBounds);

    List<StudioSlotReturnMaster> selectByExample(StudioSlotReturnMasterCriteria example);

    StudioSlotReturnMaster selectByPrimaryKey(Long studioSlotReturnMasterId);

    int updateByExampleSelective(@Param("record") StudioSlotReturnMaster record, @Param("example") StudioSlotReturnMasterCriteria example);

    int updateByExample(@Param("record") StudioSlotReturnMaster record, @Param("example") StudioSlotReturnMasterCriteria example);

    int updateByPrimaryKeySelective(StudioSlotReturnMaster record);

    int updateByPrimaryKey(StudioSlotReturnMaster record);
}