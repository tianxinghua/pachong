package com.shangpin.ephub.data.mysql.picture.spu.mapper;

import com.shangpin.ephub.data.mysql.picture.spu.po.HubSpuPic;
import com.shangpin.ephub.data.mysql.picture.spu.po.HubSpuPicCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface HubSpuPicMapper {
    int countByExample(HubSpuPicCriteria example);

    int deleteByExample(HubSpuPicCriteria example);

    int deleteByPrimaryKey(Long spuPicId);

    int insert(HubSpuPic record);

    int insertSelective(HubSpuPic record);

    List<HubSpuPic> selectByExampleWithRowbounds(HubSpuPicCriteria example, RowBounds rowBounds);

    List<HubSpuPic> selectByExample(HubSpuPicCriteria example);

    HubSpuPic selectByPrimaryKey(Long spuPicId);

    int updateByExampleSelective(@Param("record") HubSpuPic record, @Param("example") HubSpuPicCriteria example);

    int updateByExample(@Param("record") HubSpuPic record, @Param("example") HubSpuPicCriteria example);

    int updateByPrimaryKeySelective(HubSpuPic record);

    int updateByPrimaryKey(HubSpuPic record);
}