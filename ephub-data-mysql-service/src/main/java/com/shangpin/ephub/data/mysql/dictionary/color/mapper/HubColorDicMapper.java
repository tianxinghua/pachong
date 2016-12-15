package com.shangpin.ephub.data.mysql.dictionary.color.mapper;

import com.shangpin.ephub.data.mysql.dictionary.color.po.HubColorDic;
import com.shangpin.ephub.data.mysql.dictionary.color.po.HubColorDicCriteria;
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
public interface HubColorDicMapper {
	
    int countByExample(HubColorDicCriteria example);

    int deleteByExample(HubColorDicCriteria example);

    int deleteByPrimaryKey(Long colorDicId);

    int insert(HubColorDic record);

    int insertSelective(HubColorDic record);

    List<HubColorDic> selectByExampleWithRowbounds(HubColorDicCriteria example, RowBounds rowBounds);

    List<HubColorDic> selectByExample(HubColorDicCriteria example);

    HubColorDic selectByPrimaryKey(Long colorDicId);

    int updateByExampleSelective(@Param("record") HubColorDic record, @Param("example") HubColorDicCriteria example);

    int updateByExample(@Param("record") HubColorDic record, @Param("example") HubColorDicCriteria example);

    int updateByPrimaryKeySelective(HubColorDic record);

    int updateByPrimaryKey(HubColorDic record);
}