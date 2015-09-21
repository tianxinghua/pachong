package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.OrderDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by loyalty on 15/9/11.
 */
public interface OrderService {

    /**
     * 保存订单信息
     * @param orderDTO 订单DTO
     * @throws ServiceException
     */
    public void saveOrder(OrderDTO orderDTO ) throws ServiceException;

    /**
     * 获取订单状态 根据订单状态
     * @param supplierId 供货商ID
     * @param status 状态码
     * @return
     * @throws ServiceException
     */
    public List<String> getOrderIdBySupplierIdAndOrderStatus(String supplierId,String status) throws ServiceException;

    /**
     * 修改订单状态
     * @param statusMap 状态信息  ORDERID(UUID),STATUS
     * @throws ServiceException
     */
    public void updateOrderStatus(Map<String,String> statusMap) throws ServiceException;

    /**
     * 获取UUID 根据采购单ID
     * @param spOrderId
     * @return
     * @throws ServiceException
     */
    public String getUuIdByspOrderId(String spOrderId)throws ServiceException;
}
