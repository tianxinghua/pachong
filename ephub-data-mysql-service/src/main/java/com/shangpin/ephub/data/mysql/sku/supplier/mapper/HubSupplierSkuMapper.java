package com.shangpin.ephub.data.mysql.sku.supplier.mapper;

import com.shangpin.ephub.data.mysql.sku.supplier.po.HubSupplierSku;
import com.shangpin.ephub.data.mysql.sku.supplier.po.HubSupplierSkuCriteria;
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
public interface HubSupplierSkuMapper {
    int countByExample(HubSupplierSkuCriteria example);

    int deleteByExample(HubSupplierSkuCriteria example);

    int deleteByPrimaryKey(Long supplierSkuId);

    int insert(HubSupplierSku record);

    int insertSelective(HubSupplierSku record);

    List<HubSupplierSku> selectByExampleWithRowbounds(HubSupplierSkuCriteria example, RowBounds rowBounds);

    List<HubSupplierSku> selectByExample(HubSupplierSkuCriteria example);

    HubSupplierSku selectByPrimaryKey(Long supplierSkuId);

    int updateByExampleSelective(@Param("record") HubSupplierSku record, @Param("example") HubSupplierSkuCriteria example);

    int updateByExample(@Param("record") HubSupplierSku record, @Param("example") HubSupplierSkuCriteria example);

    int updateByPrimaryKeySelective(HubSupplierSku record);

    int updateByPrimaryKey(HubSupplierSku record);
}