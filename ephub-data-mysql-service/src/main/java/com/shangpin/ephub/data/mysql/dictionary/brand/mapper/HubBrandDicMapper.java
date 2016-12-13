package com.shangpin.ephub.data.mysql.dictionary.brand.mapper;

import com.shangpin.ephub.data.mysql.dictionary.brand.po.HubBrandDic;
import com.shangpin.ephub.data.mysql.dictionary.brand.po.HubBrandDicCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

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