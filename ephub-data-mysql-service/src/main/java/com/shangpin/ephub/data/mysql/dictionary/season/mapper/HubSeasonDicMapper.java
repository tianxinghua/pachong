package com.shangpin.ephub.data.mysql.dictionary.season.mapper;

import com.shangpin.ephub.data.mysql.dictionary.season.po.HubSeasonDic;
import com.shangpin.ephub.data.mysql.dictionary.season.po.HubSeasonDicCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
/**
 * <p>Title:HubBrandDicMapper.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月13日 下午2:12:01
 */
@Mapper
public interface HubSeasonDicMapper {
    int countByExample(HubSeasonDicCriteria example);

    int deleteByExample(HubSeasonDicCriteria example);

    int deleteByPrimaryKey(Long seasonDicId);

    int insert(HubSeasonDic record);

    int insertSelective(HubSeasonDic record);

    List<HubSeasonDic> selectByExampleWithRowbounds(HubSeasonDicCriteria example, RowBounds rowBounds);

    List<HubSeasonDic> selectByExample(HubSeasonDicCriteria example);

    HubSeasonDic selectByPrimaryKey(Long seasonDicId);

    int updateByExampleSelective(@Param("record") HubSeasonDic record, @Param("example") HubSeasonDicCriteria example);

    int updateByExample(@Param("record") HubSeasonDic record, @Param("example") HubSeasonDicCriteria example);

    int updateByPrimaryKeySelective(HubSeasonDic record);

    int updateByPrimaryKey(HubSeasonDic record);
}