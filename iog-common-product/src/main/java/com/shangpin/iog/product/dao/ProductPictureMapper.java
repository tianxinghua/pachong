package com.shangpin.iog.product.dao;


import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SpuDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductPictureMapper extends IBaseDao<ProductPictureDTO> {

    /**
     * 获取图片列表
     * @param supplier 供货商标识
     * @param sku      SKU标识
     * @return 图片列表
     */
    List<ProductPictureDTO> findBySupplierAndSku(@Param("supplier") String supplier,
                                                   @Param("sku") String sku
                                                  );


}