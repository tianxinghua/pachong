package com.shangpin.ephub.data.mysql.slot.dicbrand.mapper;

import com.shangpin.ephub.data.mysql.slot.dicbrand.po.HubDicStudioBrand;
import com.shangpin.ephub.data.mysql.slot.dicbrand.po.HubDicStudioBrandCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface HubDicStudioBrandMapper {
    int countByExample(HubDicStudioBrandCriteria example);

    int deleteByExample(HubDicStudioBrandCriteria example);

    int deleteByPrimaryKey(Long studioBrandId);

    int  insert(HubDicStudioBrand record);

    int  insertSelective(HubDicStudioBrand record);

    List<HubDicStudioBrand> selectByExampleWithRowbounds(HubDicStudioBrandCriteria example, RowBounds rowBounds);

    List<HubDicStudioBrand> selectByExample(HubDicStudioBrandCriteria example);

    HubDicStudioBrand selectByPrimaryKey(Long studioBrandId);

    int updateByExampleSelective(@Param("record") HubDicStudioBrand record, @Param("example") HubDicStudioBrandCriteria example);

    int updateByExample(@Param("record") HubDicStudioBrand record, @Param("example") HubDicStudioBrandCriteria example);

    int updateByPrimaryKeySelective(HubDicStudioBrand record);

    int updateByPrimaryKey(HubDicStudioBrand record);
}