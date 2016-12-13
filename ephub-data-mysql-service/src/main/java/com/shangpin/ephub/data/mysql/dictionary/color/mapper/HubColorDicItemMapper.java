package com.shangpin.ephub.data.mysql.dictionary.color.mapper;

import com.shangpin.ephub.data.mysql.dictionary.color.po.HubColorDicItem;
import com.shangpin.ephub.data.mysql.dictionary.color.po.HubColorDicItemCriteria;
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
public interface HubColorDicItemMapper {
    int countByExample(HubColorDicItemCriteria example);

    int deleteByExample(HubColorDicItemCriteria example);

    int deleteByPrimaryKey(Long colorDicItemId);

    int insert(HubColorDicItem record);

    int insertSelective(HubColorDicItem record);

    List<HubColorDicItem> selectByExampleWithRowbounds(HubColorDicItemCriteria example, RowBounds rowBounds);

    List<HubColorDicItem> selectByExample(HubColorDicItemCriteria example);

    HubColorDicItem selectByPrimaryKey(Long colorDicItemId);

    int updateByExampleSelective(@Param("record") HubColorDicItem record, @Param("example") HubColorDicItemCriteria example);

    int updateByExample(@Param("record") HubColorDicItem record, @Param("example") HubColorDicItemCriteria example);

    int updateByPrimaryKeySelective(HubColorDicItem record);

    int updateByPrimaryKey(HubColorDicItem record);
}