package com.shangpin.iog.facade.dubbo.service;

/**
 * Created by loyalty on 15/7/7.
 * 库存查询
 */
public interface StockService {

    /**
     * 获取供货商的单个商品库存
     * @param supplierId
     * @param skuId
     * @param supplierSkuId
     * @return
     */
    public int getStockForProduct(String supplierId,String skuId,String supplierSkuId);
}
