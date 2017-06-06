package com.shangpin.studio.data.mysql.mapper;

import com.shangpin.studio.data.mysql.po.StudioMatchSpu;
import com.shangpin.studio.data.mysql.po.StudioMatchSpuCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface StudioMatchSpuMapper {
    int countByExample(StudioMatchSpuCriteria example);

    int deleteByExample(StudioMatchSpuCriteria example);

    int deleteByPrimaryKey(Long studioMatchSpuId);

    int insert(StudioMatchSpu record);

    int insertSelective(StudioMatchSpu record);

    List<StudioMatchSpu> selectByExampleWithRowbounds(StudioMatchSpuCriteria example, RowBounds rowBounds);

    List<StudioMatchSpu> selectByExample(StudioMatchSpuCriteria example);

    StudioMatchSpu selectByPrimaryKey(Long studioMatchSpuId);

    int updateByExampleSelective(@Param("record") StudioMatchSpu record, @Param("example") StudioMatchSpuCriteria example);

    int updateByExample(@Param("record") StudioMatchSpu record, @Param("example") StudioMatchSpuCriteria example);

    int updateByPrimaryKeySelective(StudioMatchSpu record);

    int updateByPrimaryKey(StudioMatchSpu record);
}