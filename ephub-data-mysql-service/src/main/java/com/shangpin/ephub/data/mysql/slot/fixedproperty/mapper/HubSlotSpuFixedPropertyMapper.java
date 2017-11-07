package com.shangpin.ephub.data.mysql.slot.fixedproperty.mapper;

import com.shangpin.ephub.data.mysql.slot.fixedproperty.po.HubSlotSpuFixedProperty;
import com.shangpin.ephub.data.mysql.slot.fixedproperty.po.HubSlotSpuFixedPropertyCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface HubSlotSpuFixedPropertyMapper {
    int countByExample(HubSlotSpuFixedPropertyCriteria example);

    int deleteByExample(HubSlotSpuFixedPropertyCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(HubSlotSpuFixedProperty record);

    int insertSelective(HubSlotSpuFixedProperty record);

    List<HubSlotSpuFixedProperty> selectByExampleWithRowbounds(HubSlotSpuFixedPropertyCriteria example, RowBounds rowBounds);

    List<HubSlotSpuFixedProperty> selectByExample(HubSlotSpuFixedPropertyCriteria example);

    HubSlotSpuFixedProperty selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") HubSlotSpuFixedProperty record, @Param("example") HubSlotSpuFixedPropertyCriteria example);

    int updateByExample(@Param("record") HubSlotSpuFixedProperty record, @Param("example") HubSlotSpuFixedPropertyCriteria example);

    int updateByPrimaryKeySelective(HubSlotSpuFixedProperty record);

    int updateByPrimaryKey(HubSlotSpuFixedProperty record);
}