package com.shangpin.studio.data.mysql.mapper;

import com.shangpin.studio.data.mysql.po.dic.StudioDicSupplier;
import com.shangpin.studio.data.mysql.po.dic.StudioDicSupplierCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface StudioDicSupplierMapper {
    int countByExample(StudioDicSupplierCriteria example);

    int deleteByExample(StudioDicSupplierCriteria example);

    int deleteByPrimaryKey(Long supplierStudioId);

    int insert(StudioDicSupplier record);

    int insertSelective(StudioDicSupplier record);

    List<StudioDicSupplier> selectByExampleWithRowbounds(StudioDicSupplierCriteria example, RowBounds rowBounds);

    List<StudioDicSupplier> selectByExample(StudioDicSupplierCriteria example);

    StudioDicSupplier selectByPrimaryKey(Long supplierStudioId);

    int updateByExampleSelective(@Param("record") StudioDicSupplier record, @Param("example") StudioDicSupplierCriteria example);

    int updateByExample(@Param("record") StudioDicSupplier record, @Param("example") StudioDicSupplierCriteria example);

    int updateByPrimaryKeySelective(StudioDicSupplier record);

    int updateByPrimaryKey(StudioDicSupplier record);
}