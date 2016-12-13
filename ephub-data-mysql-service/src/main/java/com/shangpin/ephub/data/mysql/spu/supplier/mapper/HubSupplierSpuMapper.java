package com.shangpin.ephub.data.mysql.spu.supplier.mapper;

import com.shangpin.ephub.data.mysql.spu.supplier.po.HubSupplierSpu;
import com.shangpin.ephub.data.mysql.spu.supplier.po.HubSupplierSpuCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface HubSupplierSpuMapper {
    int countByExample(HubSupplierSpuCriteria example);

    int deleteByExample(HubSupplierSpuCriteria example);

    int deleteByPrimaryKey(Long supplierSpuId);

    int insert(HubSupplierSpu record);

    int insertSelective(HubSupplierSpu record);

    List<HubSupplierSpu> selectByExampleWithRowbounds(HubSupplierSpuCriteria example, RowBounds rowBounds);

    List<HubSupplierSpu> selectByExample(HubSupplierSpuCriteria example);

    HubSupplierSpu selectByPrimaryKey(Long supplierSpuId);

    int updateByExampleSelective(@Param("record") HubSupplierSpu record, @Param("example") HubSupplierSpuCriteria example);

    int updateByExample(@Param("record") HubSupplierSpu record, @Param("example") HubSupplierSpuCriteria example);

    int updateByPrimaryKeySelective(HubSupplierSpu record);

    int updateByPrimaryKey(HubSupplierSpu record);
}