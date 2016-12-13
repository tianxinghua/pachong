package com.shangpin.ephub.data.mysql.supplier.config.mapper;

import com.shangpin.ephub.data.mysql.supplier.config.po.HubSupplierConfig;
import com.shangpin.ephub.data.mysql.supplier.config.po.HubSupplierConfigCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface HubSupplierConfigMapper {
    int countByExample(HubSupplierConfigCriteria example);

    int deleteByExample(HubSupplierConfigCriteria example);

    int deleteByPrimaryKey(Long supplierConfigId);

    int insert(HubSupplierConfig record);

    int insertSelective(HubSupplierConfig record);

    List<HubSupplierConfig> selectByExampleWithRowbounds(HubSupplierConfigCriteria example, RowBounds rowBounds);

    List<HubSupplierConfig> selectByExample(HubSupplierConfigCriteria example);

    HubSupplierConfig selectByPrimaryKey(Long supplierConfigId);

    int updateByExampleSelective(@Param("record") HubSupplierConfig record, @Param("example") HubSupplierConfigCriteria example);

    int updateByExample(@Param("record") HubSupplierConfig record, @Param("example") HubSupplierConfigCriteria example);

    int updateByPrimaryKeySelective(HubSupplierConfig record);

    int updateByPrimaryKey(HubSupplierConfig record);
}