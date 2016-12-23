package com.shangpin.ephub.data.mysql.dictionary.origin.mapper;

import com.shangpin.ephub.data.mysql.dictionary.origin.po.HubOriginDic;
import com.shangpin.ephub.data.mysql.dictionary.origin.po.HubOriginDicCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
/**
 * <p>Title:HubOriginDicMapper.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月22日 下午12:24:39
 */
@Mapper
public interface HubOriginDicMapper {
    int countByExample(HubOriginDicCriteria example);

    int deleteByExample(HubOriginDicCriteria example);

    int deleteByPrimaryKey(Long originDicId);

    int insert(HubOriginDic record);

    int insertSelective(HubOriginDic record);

    List<HubOriginDic> selectByExampleWithRowbounds(HubOriginDicCriteria example, RowBounds rowBounds);

    List<HubOriginDic> selectByExample(HubOriginDicCriteria example);

    HubOriginDic selectByPrimaryKey(Long originDicId);

    int updateByExampleSelective(@Param("record") HubOriginDic record, @Param("example") HubOriginDicCriteria example);

    int updateByExample(@Param("record") HubOriginDic record, @Param("example") HubOriginDicCriteria example);

    int updateByPrimaryKeySelective(HubOriginDic record);

    int updateByPrimaryKey(HubOriginDic record);
}