package com.shangpin.iog.product.dao;


import java.util.List;
import java.util.Map;

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
    /**
	 * 保存产品库存
	 * @param stockDTO skuNo,suplierId,quantity必须有
	 * @return
	 */
    int saveStock(SupplierStockDTO stockDTO);
    
    public SupplierStockDTO findSingleStock(@Param(value="supplierId")  String supplierId );
   
    public SupplierStockDTO findBySkuAndSupplier(Map<String,String> map);
    
    
    
    public List<SupplierStockDTO> findAllSupplierStock();
}