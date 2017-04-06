package com.shangpin.ephub.data.mysql.sku.price.mapper;

import com.shangpin.ephub.data.mysql.sku.price.po.HubSupplierPriceChangeRecord;
import com.shangpin.ephub.data.mysql.sku.price.po.HubSupplierPriceChangeRecordCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
 @Mapper
public interface HubSupplierPriceChangeRecordMapper {
    int countByExample(HubSupplierPriceChangeRecordCriteria example);

    int deleteByExample(HubSupplierPriceChangeRecordCriteria example);

    int deleteByPrimaryKey(Long supplierPriceChangeRecordId);

    int insert(HubSupplierPriceChangeRecord record);

    int insertSelective(HubSupplierPriceChangeRecord record);

    List<HubSupplierPriceChangeRecord> selectByExampleWithRowbounds(HubSupplierPriceChangeRecordCriteria example, RowBounds rowBounds);

    List<HubSupplierPriceChangeRecord> selectByExample(HubSupplierPriceChangeRecordCriteria example);

    HubSupplierPriceChangeRecord selectByPrimaryKey(Long supplierPriceChangeRecordId);

    int updateByExampleSelective(@Param("record") HubSupplierPriceChangeRecord record, @Param("example") HubSupplierPriceChangeRecordCriteria example);

    int updateByExample(@Param("record") HubSupplierPriceChangeRecord record, @Param("example") HubSupplierPriceChangeRecordCriteria example);

    int updateByPrimaryKeySelective(HubSupplierPriceChangeRecord record);

    int updateByPrimaryKey(HubSupplierPriceChangeRecord record);
}