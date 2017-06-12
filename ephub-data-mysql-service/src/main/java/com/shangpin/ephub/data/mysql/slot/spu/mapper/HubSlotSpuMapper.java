package com.shangpin.ephub.data.mysql.slot.spu.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.shangpin.ephub.data.mysql.slot.spu.po.HubSlotSpu;
import com.shangpin.ephub.data.mysql.slot.spu.po.HubSlotSpuCriteria;

@Mapper
public interface HubSlotSpuMapper {
    int countByExample(HubSlotSpuCriteria example);

    int deleteByExample(HubSlotSpuCriteria example);

    int deleteByPrimaryKey(Long slotSpuId);

    int insert(HubSlotSpu record);

    int insertSelective(HubSlotSpu record);

    List<HubSlotSpu> selectByExampleWithRowbounds(HubSlotSpuCriteria example, RowBounds rowBounds);

    List<HubSlotSpu> selectByExample(HubSlotSpuCriteria example);

    HubSlotSpu selectByPrimaryKey(Long slotSpuId);

    int updateByExampleSelective(@Param("record") HubSlotSpu record, @Param("example") HubSlotSpuCriteria example);

    int updateByExample(@Param("record") HubSlotSpu record, @Param("example") HubSlotSpuCriteria example);

    int updateByPrimaryKeySelective(HubSlotSpu record);

    int updateByPrimaryKey(HubSlotSpu record);
}