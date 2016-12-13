package com.shangpin.ephub.data.mysql.mapping.sku.mapper;

import com.shangpin.ephub.data.mysql.mapping.sku.po.HubSkuSupplierMapping;
import com.shangpin.ephub.data.mysql.mapping.sku.po.HubSkuSupplierMappingCriteria;
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
public interface HubSkuSupplierMappingMapper {
    int countByExample(HubSkuSupplierMappingCriteria example);

    int deleteByExample(HubSkuSupplierMappingCriteria example);

    int deleteByPrimaryKey(Long skuSupplierMappingId);

    int insert(HubSkuSupplierMapping record);

    int insertSelective(HubSkuSupplierMapping record);

    List<HubSkuSupplierMapping> selectByExampleWithRowbounds(HubSkuSupplierMappingCriteria example, RowBounds rowBounds);

    List<HubSkuSupplierMapping> selectByExample(HubSkuSupplierMappingCriteria example);

    HubSkuSupplierMapping selectByPrimaryKey(Long skuSupplierMappingId);

    int updateByExampleSelective(@Param("record") HubSkuSupplierMapping record, @Param("example") HubSkuSupplierMappingCriteria example);

    int updateByExample(@Param("record") HubSkuSupplierMapping record, @Param("example") HubSkuSupplierMappingCriteria example);

    int updateByPrimaryKeySelective(HubSkuSupplierMapping record);

    int updateByPrimaryKey(HubSkuSupplierMapping record);
}