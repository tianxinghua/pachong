package com.shangpin.studio.data.mysql.mapper;

import com.shangpin.studio.data.mysql.po.StudioSlotDefectiveSpuPic;
import com.shangpin.studio.data.mysql.po.StudioSlotDefectiveSpuPicCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface StudioSlotDefectiveSpuPicMapper {
    int countByExample(StudioSlotDefectiveSpuPicCriteria example);

    int deleteByExample(StudioSlotDefectiveSpuPicCriteria example);

    int deleteByPrimaryKey(Long studioSlotDefectiveSpuPicId);

    int insert(StudioSlotDefectiveSpuPic record);

    int insertSelective(StudioSlotDefectiveSpuPic record);

    List<StudioSlotDefectiveSpuPic> selectByExampleWithRowbounds(StudioSlotDefectiveSpuPicCriteria example, RowBounds rowBounds);

    List<StudioSlotDefectiveSpuPic> selectByExample(StudioSlotDefectiveSpuPicCriteria example);

    StudioSlotDefectiveSpuPic selectByPrimaryKey(Long studioSlotDefectiveSpuPicId);

    int updateByExampleSelective(@Param("record") StudioSlotDefectiveSpuPic record, @Param("example") StudioSlotDefectiveSpuPicCriteria example);

    int updateByExample(@Param("record") StudioSlotDefectiveSpuPic record, @Param("example") StudioSlotDefectiveSpuPicCriteria example);

    int updateByPrimaryKeySelective(StudioSlotDefectiveSpuPic record);

    int updateByPrimaryKey(StudioSlotDefectiveSpuPic record);
}