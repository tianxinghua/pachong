package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SkuPriceDTO;

import java.util.Map;


/**
 * Created by lizhongren on 2015/8/29.
 * 价格服务
 */
public interface SkuPriceService {
    /**
     * 根据供应商编号 查询SKU和marketPrice、suppplierPrice
     * map格式是 <skuId,marketPrice|supplierPrice>
     * @param supplierId    供货商编号
     * @return
     */
    public Map<String ,String>  getSkuPriceMap(String supplierId) throws ServiceException;

    /**
     * 根据供货商编号 和 skuid 查询市场价和供货价
     * @param supplierId   供货商编号
     * @param skuId         SKUId
     * @return     marketPrice|supplierPrice
     * @throws ServiceException
     */
    public String getSkuPrice(String supplierId,String skuId) throws ServiceException;

    /**
     * 更新价格
     * @param skuPriceDTO 对象
     * @throws ServiceException
     */
    public void updatePrice(SkuPriceDTO skuPriceDTO) throws ServiceException;
    /**
     * 获取最新价格
     * @param supplierId 
     * @throws ServiceException
     */
    public Map<String,Map<String,String>> getNewSkuPrice(String supplierId) throws ServiceException;
    
    /**
     * 获取最新价格
     * @param supplierId 
     * @throws ServiceException
     */
    public SkuDTO findSupplierPrice(String supplierId,String skuId) throws ServiceException;
}
