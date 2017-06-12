package com.shangpin.ephub.data.mysql.slot.appendix.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.shangpin.ephub.data.mysql.slot.appendix.po.HubSlotSpuAppendix;
import com.shangpin.ephub.data.mysql.slot.appendix.po.HubSlotSpuAppendixCriteria;
@Mapper
public interface HubSlotSpuAppendixMapper {
    int countByExample(HubSlotSpuAppendixCriteria example);

    int deleteByExample(HubSlotSpuAppendixCriteria example);

    int deleteByPrimaryKey(Long spuAppendixId);

    int insert(HubSlotSpuAppendix record);

    int insertSelective(HubSlotSpuAppendix record);

    List<HubSlotSpuAppendix> selectByExampleWithRowbounds(HubSlotSpuAppendixCriteria example, RowBounds rowBounds);

    List<HubSlotSpuAppendix> selectByExample(HubSlotSpuAppendixCriteria example);

    HubSlotSpuAppendix selectByPrimaryKey(Long spuAppendixId);

    int updateByExampleSelective(@Param("record") HubSlotSpuAppendix record, @Param("example") HubSlotSpuAppendixCriteria example);

    int updateByExample(@Param("record") HubSlotSpuAppendix record, @Param("example") HubSlotSpuAppendixCriteria example);

    int updateByPrimaryKeySelective(HubSlotSpuAppendix record);

    int updateByPrimaryKey(HubSlotSpuAppendix record);
}