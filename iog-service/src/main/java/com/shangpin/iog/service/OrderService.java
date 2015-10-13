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
     * 更新订单信息
     * @param orderDTO
     * @throws ServiceException
     */
    public void update(OrderDTO orderDTO) throws ServiceException;

    /**
     * 获取订单状态 根据订单ID 和 订单状态
     * @param supplierId 供货商ID
     * @param status 状态码
     * @return
     * @throws ServiceException
     */
    public List<String> getOrderIdBySupplierIdAndOrderStatus(String supplierId,String status) throws ServiceException;

    /**
     * 获取订单 根据订单ID 和 订单状态
     * @param supplierId 供货商ID
     * @param status 状态码
     * @return
     * @throws ServiceException
     */
    public List<OrderDTO> getOrderBySupplierIdAndOrderStatus(String supplierId,String status) throws ServiceException;

    /**
     * 获取异常订单信息
     * @return
     * @throws ServiceException
     */
    public List<OrderDTO> getExceptionOrder() throws ServiceException;

    /**
     * 更改订单信息
     * @param statusMap 比较全的信息
     *   <set>
    <if test="status != null"> STATUS = #{status}, </if>
    <if test="memo != null"> MEMO = #{memo}, </if>
    <if test="updateTime != null"> UPDATE_TIME = #{updateTime}, </if>
    <if test="supplierOrderNo != null"> SUPPLIER_ORDER_NO = #{supplierOrderNo}, </if>
    <if test="deliveryNo != null"> DELIVERY_NO = #{deliveryNo},</if>
    <if test="excState != null"> EXC_STATE = #{excState}, </if>
    <if test="excDesc != null"> EXC_DESC = #{excDesc}, </if>
    <if test="excTime != null"> EXC_TIME = #{excTime}</if>



    </set>
    where UUID = #{uuid}
     *
     * @throws ServiceException
     */
    public void updateOrderMsg(Map<String,String> statusMap) throws ServiceException;

    /**
     * 修改订单状态
     * @param statusMap 状态信息  ORDERID(UUID),STATUS
     * @throws ServiceException
     */
    public void updateOrderStatus(Map<String,String> statusMap) throws ServiceException;

    /**
     * 增加异常信息
     * @param exceptionMap key 包括 excState，excDesc,excTime,uuid
     * @throws ServiceException
     */
    public void  updateExceptionMsg(Map<String,String> exceptionMap) throws ServiceException;

    /**
     * 增加发货单
     *
     *  <set>
     <if test="status != null"> STATUS = #{status}, </if>
     <if test="updateTime != null"> UPDATE_TIME = #{updateTime}, </if>
     <if test="deliveryNo != null"> DELIVERY_NO = #{deliveryNo}</if>


     </set>
     where UUID = #{uuid}
     * @param exceptionMap
     * @throws ServiceException
     */
    public void updateDeliveryNo(Map<String,String> exceptionMap) throws ServiceException;




    /**
     * 获取UUID 根据订单ID
     * @param spOrderId
     * @return
     * @throws ServiceException
     */
    public String getUuIdBySpOrderId(String spOrderId)throws ServiceException;

    /**
     * 根据采购单获取订单信息
     * @param purchaseNo ：采购单编号
     * @return
     * @throws ServiceException
     */
    public OrderDTO getOrderByPurchaseNo(String purchaseNo) throws ServiceException;

    /**
     * 根据订单号获取订单信息
     * @param orderNo  :订单编号
     * @return
     * @throws ServiceException
     */
    public OrderDTO getOrderByOrderNo(String orderNo) throws ServiceException;

    /**
     * 根据唯一编号获取订单信息
     * @param uuid
     * @return
     * @throws ServiceException
     */
    public OrderDTO getOrderByUuId(String uuid) throws ServiceException;

}
