package com.shangpin.ephub.data.mysql.slot.supplier.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.shangpin.ephub.data.mysql.slot.supplier.po.HubSlotSpuSupplier;
import com.shangpin.ephub.data.mysql.slot.supplier.po.HubSlotSpuSupplierCriteria;

@Mapper
public interface HubSlotSpuSupplierMapper {
    int countByExample(HubSlotSpuSupplierCriteria example);

    int deleteByExample(HubSlotSpuSupplierCriteria example);

    int deleteByPrimaryKey(Long slotSpuSupplierId);

    int insert(HubSlotSpuSupplier record);

    int insertSelective(HubSlotSpuSupplier record);

    List<HubSlotSpuSupplier> selectByExampleWithRowbounds(HubSlotSpuSupplierCriteria example, RowBounds rowBounds);

    List<HubSlotSpuSupplier> selectByExample(HubSlotSpuSupplierCriteria example);

    HubSlotSpuSupplier selectByPrimaryKey(Long slotSpuSupplierId);

    int updateByExampleSelective(@Param("record") HubSlotSpuSupplier record, @Param("example") HubSlotSpuSupplierCriteria example);

    int updateByExample(@Param("record") HubSlotSpuSupplier record, @Param("example") HubSlotSpuSupplierCriteria example);

    int updateByPrimaryKeySelective(HubSlotSpuSupplier record);

    int updateByPrimaryKey(HubSlotSpuSupplier record);
}