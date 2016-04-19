package com.shangpin.iog.facade.dubbo.service;

import com.shangpin.iog.facade.dubbo.dto.ServiceException;

/**
 * Created by lizhongren on 2015/11/9.
 * 供货商的订单接口
 */
public interface OrderOfSupplierService {
    /**
     * 下单锁库存接口
     * @param orderMessage 订单信息
     * @return  返回信息 包含是否成功
     */
    public String createOrder(String supplierNo,String orderMessage) ;
}
