package com.shangpin.studio.data.mysql.mapper;

import com.shangpin.studio.data.mysql.po.StudioSlotReturnDetail;
import com.shangpin.studio.data.mysql.po.StudioSlotReturnDetailCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface StudioSlotReturnDetailMapper {
    int countByExample(StudioSlotReturnDetailCriteria example);

    int deleteByExample(StudioSlotReturnDetailCriteria example);

    int deleteByPrimaryKey(Long studioSlotReturnDetailId);

    int insert(StudioSlotReturnDetail record);

    int insertSelective(StudioSlotReturnDetail record);

    List<StudioSlotReturnDetail> selectByExampleWithRowbounds(StudioSlotReturnDetailCriteria example, RowBounds rowBounds);

    List<StudioSlotReturnDetail> selectByExample(StudioSlotReturnDetailCriteria example);

    StudioSlotReturnDetail selectByPrimaryKey(Long studioSlotReturnDetailId);

    int updateByExampleSelective(@Param("record") StudioSlotReturnDetail record, @Param("example") StudioSlotReturnDetailCriteria example);

    int updateByExample(@Param("record") StudioSlotReturnDetail record, @Param("example") StudioSlotReturnDetailCriteria example);

    int updateByPrimaryKeySelective(StudioSlotReturnDetail record);

    int updateByPrimaryKey(StudioSlotReturnDetail record);
}