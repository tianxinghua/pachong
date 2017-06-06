package com.shangpin.studio.data.mysql.mapper;

import com.shangpin.studio.data.mysql.po.StudioSlotSpuSendDetail;
import com.shangpin.studio.data.mysql.po.StudioSlotSpuSendDetailCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface StudioSlotSpuSendDetailMapper {
    int countByExample(StudioSlotSpuSendDetailCriteria example);

    int deleteByExample(StudioSlotSpuSendDetailCriteria example);

    int deleteByPrimaryKey(Long studioSlotSpuSendDetailId);

    int insert(StudioSlotSpuSendDetail record);

    int insertSelective(StudioSlotSpuSendDetail record);

    List<StudioSlotSpuSendDetail> selectByExampleWithRowbounds(StudioSlotSpuSendDetailCriteria example, RowBounds rowBounds);

    List<StudioSlotSpuSendDetail> selectByExample(StudioSlotSpuSendDetailCriteria example);

    StudioSlotSpuSendDetail selectByPrimaryKey(Long studioSlotSpuSendDetailId);

    int updateByExampleSelective(@Param("record") StudioSlotSpuSendDetail record, @Param("example") StudioSlotSpuSendDetailCriteria example);

    int updateByExample(@Param("record") StudioSlotSpuSendDetail record, @Param("example") StudioSlotSpuSendDetailCriteria example);

    int updateByPrimaryKeySelective(StudioSlotSpuSendDetail record);

    int updateByPrimaryKey(StudioSlotSpuSendDetail record);
}