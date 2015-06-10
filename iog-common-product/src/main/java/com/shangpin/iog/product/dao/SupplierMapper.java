package com.shangpin.iog.product.dao;


import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SupplierDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SupplierMapper extends IBaseDao<SupplierDTO> {

    /**
     * 获取所有供货商信息
     * @param state 状态编码
     * @return 供货商列表
     */

    List<SupplierDTO> findByState(@Param("state") String state );


}