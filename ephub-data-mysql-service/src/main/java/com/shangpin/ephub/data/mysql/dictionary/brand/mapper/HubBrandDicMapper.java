package com.shangpin.ephub.data.mysql.dictionary.brand.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.shangpin.ephub.data.mysql.dictionary.brand.po.HubBrandDic;
import com.shangpin.ephub.data.mysql.dictionary.brand.po.HubBrandDicCriteria;
/**
 * <p>Title:HubBrandDicMapper.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月13日 下午2:12:01
 */
@Mapper
public interface HubBrandDicMapper {
    int countByExample(HubBrandDicCriteria example);

    int deleteByExample(HubBrandDicCriteria example);

    int deleteByPrimaryKey(Long brandDicId);

    int insert(HubBrandDic record);

    int insertSelective(HubBrandDic record);

    List<HubBrandDic> selectByExampleWithRowbounds(HubBrandDicCriteria example, RowBounds rowBounds);

    List<HubBrandDic> selectByExample(HubBrandDicCriteria example);

    HubBrandDic selectByPrimaryKey(Long brandDicId);

    int updateByExampleSelective(@Param("record") HubBrandDic record, @Param("example") HubBrandDicCriteria example);

    int updateByExample(@Param("record") HubBrandDic record, @Param("example") HubBrandDicCriteria example);

    int updateByPrimaryKeySelective(HubBrandDic record);

    int updateByPrimaryKey(HubBrandDic record);
}