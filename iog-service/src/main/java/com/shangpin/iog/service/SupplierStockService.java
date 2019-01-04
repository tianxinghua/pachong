package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.dto.SupplierStockDTO;

import java.util.List;


/**
 * Created by loyalty on 15/6/9.
 */
public interface SupplierStockService {
	/**
	 * 更新产品库存
	 * @param stockDTO skuNo,suplierId,quantity必须有
	 * @return
	 */
	void updateStock(SupplierStockDTO stockDTO) throws ServiceMessageException ;
    /**
	 * 保存产品库存
	 * @param stockDTO skuNo,suplierId,quantity必须有
	 * @return
	 */
	void saveStock(SupplierStockDTO stockDTO) throws ServiceMessageException ;

    SupplierStockDTO findSingleStock(String skuNo,String supplierId) throws ServiceMessageException ;


    List<SupplierStockDTO> findAll() throws ServiceMessageException ;

    /**
     * 通过供应商ID获取库存
     * @param supplierId
     * @return
     * @throws ServiceMessageException
     */
    List<SupplierStockDTO> findBySupplierId(String supplierId) throws ServiceMessageException ;
}
