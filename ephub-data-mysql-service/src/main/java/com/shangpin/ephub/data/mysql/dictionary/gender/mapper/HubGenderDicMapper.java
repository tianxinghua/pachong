package com.shangpin.ephub.data.mysql.dictionary.gender.mapper;

import com.shangpin.ephub.data.mysql.dictionary.gender.po.HubGenderDic;
import com.shangpin.ephub.data.mysql.dictionary.gender.po.HubGenderDicCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface HubGenderDicMapper {
    int countByExample(HubGenderDicCriteria example);

    int deleteByExample(HubGenderDicCriteria example);

    int deleteByPrimaryKey(Long genderDicId);

    int insert(HubGenderDic record);

    int insertSelective(HubGenderDic record);

    List<HubGenderDic> selectByExampleWithRowbounds(HubGenderDicCriteria example, RowBounds rowBounds);

    List<HubGenderDic> selectByExample(HubGenderDicCriteria example);

    HubGenderDic selectByPrimaryKey(Long genderDicId);

    int updateByExampleSelective(@Param("record") HubGenderDic record, @Param("example") HubGenderDicCriteria example);

    int updateByExample(@Param("record") HubGenderDic record, @Param("example") HubGenderDicCriteria example);

    int updateByPrimaryKeySelective(HubGenderDic record);

    int updateByPrimaryKey(HubGenderDic record);
}