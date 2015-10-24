package com.shangpin.iog.product.dao;


import java.util.List;

import org.springframework.data.repository.query.Param;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.SupplierStockDTO;

@Mapper
public interface SupplierStockMapper extends IBaseDao<SupplierStockDTO> {

   
	/**
	 * 更新产品库存
	 * @param stockDTO skuNo,suplierId,quantity必须有
	 * @return
	 */
    int updateStock(SupplierStockDTO stockDTO);

    List<SupplierStockDTO> findBySkuNoAndSupplierId(@Param("skuNo")String skuNo,@Param("supplierId")String supplierId);
}