package com.shangpin.ephub.data.mysql.slot.pic.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.shangpin.ephub.data.mysql.slot.pic.po.HubSlotSpuPic;
import com.shangpin.ephub.data.mysql.slot.pic.po.HubSlotSpuPicCriteria;

@Mapper
public interface HubSlotSpuPicMapper {
    int countByExample(HubSlotSpuPicCriteria example);

    int deleteByExample(HubSlotSpuPicCriteria example);

    int deleteByPrimaryKey(Long slotSpuPicId);

    int insert(HubSlotSpuPic record);

    int insertSelective(HubSlotSpuPic record);

    List<HubSlotSpuPic> selectByExampleWithRowbounds(HubSlotSpuPicCriteria example, RowBounds rowBounds);

    List<HubSlotSpuPic> selectByExample(HubSlotSpuPicCriteria example);

    HubSlotSpuPic selectByPrimaryKey(Long slotSpuPicId);

    int updateByExampleSelective(@Param("record") HubSlotSpuPic record, @Param("example") HubSlotSpuPicCriteria example);

    int updateByExample(@Param("record") HubSlotSpuPic record, @Param("example") HubSlotSpuPicCriteria example);

    int updateByPrimaryKeySelective(HubSlotSpuPic record);

    int updateByPrimaryKey(HubSlotSpuPic record);
}