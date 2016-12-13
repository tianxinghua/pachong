package com.shangpin.ephub.data.mysql.dictionary.material.mapper;

import com.shangpin.ephub.data.mysql.dictionary.material.po.HubMaterialDicItem;
import com.shangpin.ephub.data.mysql.dictionary.material.po.HubMaterialDicItemCriteria;
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
public interface HubMaterialDicItemMapper {
    int countByExample(HubMaterialDicItemCriteria example);

    int deleteByExample(HubMaterialDicItemCriteria example);

    int deleteByPrimaryKey(Long materialDicItemId);

    int insert(HubMaterialDicItem record);

    int insertSelective(HubMaterialDicItem record);

    List<HubMaterialDicItem> selectByExampleWithRowbounds(HubMaterialDicItemCriteria example, RowBounds rowBounds);

    List<HubMaterialDicItem> selectByExample(HubMaterialDicItemCriteria example);

    HubMaterialDicItem selectByPrimaryKey(Long materialDicItemId);

    int updateByExampleSelective(@Param("record") HubMaterialDicItem record, @Param("example") HubMaterialDicItemCriteria example);

    int updateByExample(@Param("record") HubMaterialDicItem record, @Param("example") HubMaterialDicItemCriteria example);

    int updateByPrimaryKeySelective(HubMaterialDicItem record);

    int updateByPrimaryKey(HubMaterialDicItem record);
}