package com.shangpin.ephub.data.mysql.supplier.token.mapper;

import com.shangpin.ephub.data.mysql.supplier.token.bean.SupplierToken;
import com.shangpin.ephub.data.mysql.supplier.token.bean.SupplierTokenCriteria;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface SupplierTokenMapper {
    int countByExample(SupplierTokenCriteria example);

    int deleteByExample(SupplierTokenCriteria example);

    int insert(SupplierToken record);

    int insertSelective(SupplierToken record);

    List<SupplierToken> selectByExampleWithRowbounds(SupplierTokenCriteria example, RowBounds rowBounds);

    List<SupplierToken> selectByExample(SupplierTokenCriteria example);

    int updateByExampleSelective(@Param("record") SupplierToken record, @Param("example") SupplierTokenCriteria example);

    int updateByExample(@Param("record") SupplierToken record, @Param("example") SupplierTokenCriteria example);

    SupplierToken selectBySupplierId(String supplierId);


    int updateBySupplierToken(SupplierToken record);

    int deleteByPrimaryKey(String supplierId);
}