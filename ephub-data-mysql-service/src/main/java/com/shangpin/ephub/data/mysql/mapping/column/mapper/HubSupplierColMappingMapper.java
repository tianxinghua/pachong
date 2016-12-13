package com.shangpin.ephub.data.mysql.mapping.column.mapper;

import com.shangpin.ephub.data.mysql.mapping.column.po.HubSupplierColMapping;
import com.shangpin.ephub.data.mysql.mapping.column.po.HubSupplierColMappingCriteria;
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
public interface HubSupplierColMappingMapper {
    int countByExample(HubSupplierColMappingCriteria example);

    int deleteByExample(HubSupplierColMappingCriteria example);

    int deleteByPrimaryKey(Long supplierColMappingId);

    int insert(HubSupplierColMapping record);

    int insertSelective(HubSupplierColMapping record);

    List<HubSupplierColMapping> selectByExampleWithRowbounds(HubSupplierColMappingCriteria example, RowBounds rowBounds);

    List<HubSupplierColMapping> selectByExample(HubSupplierColMappingCriteria example);

    HubSupplierColMapping selectByPrimaryKey(Long supplierColMappingId);

    int updateByExampleSelective(@Param("record") HubSupplierColMapping record, @Param("example") HubSupplierColMappingCriteria example);

    int updateByExample(@Param("record") HubSupplierColMapping record, @Param("example") HubSupplierColMappingCriteria example);

    int updateByPrimaryKeySelective(HubSupplierColMapping record);

    int updateByPrimaryKey(HubSupplierColMapping record);
}