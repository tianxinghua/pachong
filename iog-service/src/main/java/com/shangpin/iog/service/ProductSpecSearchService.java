package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.ProductOfSpecDTO;

/**
 * Created by lizhongren on 2016/2/17.
 */
public interface ProductSpecSearchService {

    /**
     * 获取产品信息 基础信息
     * @param supplierId   供货商
     * @param skuId         供货商SKUID
     * @return
     * @throws ServiceException
     */
    public ProductOfSpecDTO findProductBySupplierIdAndSkuId(String supplierId, String skuId) throws ServiceException;

}
