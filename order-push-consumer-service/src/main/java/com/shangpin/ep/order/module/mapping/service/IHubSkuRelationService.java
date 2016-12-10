package com.shangpin.ep.order.module.mapping.service;

import com.shangpin.ep.order.module.mapping.bean.HubSkuRelation;

/**
 * <p>Title:IHubSkuRelationService.java </p>
 * <p>Description: HUB订单系统商品sku和供货商sku之间的映射关系接口规范</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午1:59:39
 */
public interface IHubSkuRelationService {
    /**
     * 获取对应关系
     * @param supplierId ： 供货商ID
     * @param spSkuNo     :  尚品的SKUNO
     * @return
     */
    public HubSkuRelation getSkuRelationBySupplierIdAndSpSkuNo(String supplierId,String spSkuNo);

}
