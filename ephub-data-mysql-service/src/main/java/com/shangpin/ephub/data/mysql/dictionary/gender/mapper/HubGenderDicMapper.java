package com.shangpin.ephub.data.mysql.dictionary.gender.mapper;

import com.shangpin.ephub.data.mysql.dictionary.gender.po.HubGenderDic;
import com.shangpin.ephub.data.mysql.dictionary.gender.po.HubGenderDicCriteria;
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