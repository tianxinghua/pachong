package com.shangpin.ephub.data.mysql.mapping.material.mapper;

import com.shangpin.ephub.data.mysql.mapping.material.po.HubMaterialMapping;
import com.shangpin.ephub.data.mysql.mapping.material.po.HubMaterialMappingCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
/**
 * <p>Title:HubMaterialMappingMapper.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月22日 上午11:56:13
 */
@Mapper
public interface HubMaterialMappingMapper {
    int countByExample(HubMaterialMappingCriteria example);

    int deleteByExample(HubMaterialMappingCriteria example);

    int deleteByPrimaryKey(Long materialMappingId);

    int insert(HubMaterialMapping record);

    int insertSelective(HubMaterialMapping record);

    List<HubMaterialMapping> selectByExampleWithRowbounds(HubMaterialMappingCriteria example, RowBounds rowBounds);

    List<HubMaterialMapping> selectByExample(HubMaterialMappingCriteria example);

    HubMaterialMapping selectByPrimaryKey(Long materialMappingId);

    int updateByExampleSelective(@Param("record") HubMaterialMapping record, @Param("example") HubMaterialMappingCriteria example);

    int updateByExample(@Param("record") HubMaterialMapping record, @Param("example") HubMaterialMappingCriteria example);

    int updateByPrimaryKeySelective(HubMaterialMapping record);

    int updateByPrimaryKey(HubMaterialMapping record);
}