package com.shangpin.ephub.data.mysql.mapping.value.mapper;

import com.shangpin.ephub.data.mysql.mapping.value.po.HubSupplierValueMapping;
import com.shangpin.ephub.data.mysql.mapping.value.po.HubSupplierValueMappingCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
/**
 * <p>Title:HubSupplierValueMappingMapper.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月23日 下午1:46:35
 */
@Mapper
public interface HubSupplierValueMappingMapper {
    int countByExample(HubSupplierValueMappingCriteria example);

    int deleteByExample(HubSupplierValueMappingCriteria example);

    int deleteByPrimaryKey(Long hubSupplierValMappingId);

    int insert(HubSupplierValueMapping record);

    int insertSelective(HubSupplierValueMapping record);

    List<HubSupplierValueMapping> selectByExampleWithRowbounds(HubSupplierValueMappingCriteria example, RowBounds rowBounds);

    List<HubSupplierValueMapping> selectByExample(HubSupplierValueMappingCriteria example);

    HubSupplierValueMapping selectByPrimaryKey(Long hubSupplierValMappingId);

    int updateByExampleSelective(@Param("record") HubSupplierValueMapping record, @Param("example") HubSupplierValueMappingCriteria example);

    int updateByExample(@Param("record") HubSupplierValueMapping record, @Param("example") HubSupplierValueMappingCriteria example);

    int updateByPrimaryKeySelective(HubSupplierValueMapping record);

    int updateByPrimaryKey(HubSupplierValueMapping record);
}