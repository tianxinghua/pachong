package com.shangpin.ep.order.module.sku.service;

import com.shangpin.ep.order.module.sku.bean.HubSku;

/**
 * <p>Title:IHubSku.java </p>
 * <p>Description: 涉及到一些sku的service方法 </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午2:46:57
 */
public interface IHubSkuService {

	/**
     * 根据供应商门户编号和供应商skuid查找SKU
     * @param supplierId
     * @param supplierSkuId
     * @return
     */
    public HubSku getSku(String supplierId,String supplierSkuId);
}
