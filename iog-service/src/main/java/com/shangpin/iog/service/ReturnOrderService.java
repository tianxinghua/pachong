package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.ReturnOrderDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by sunny on 2015/9/19.
 */
public interface ReturnOrderService {
    /**
     * 保存退单信息
     * @param returnOrderDTO 退单DTO
     * @throws ServiceException
     */
    public void saveOrder(ReturnOrderDTO returnOrderDTO ) throws ServiceException;


    /**
     * 保存退单信息
     * @param returnOrderDTO 退单DTO
     * @throws ServiceException
     */
    public boolean saveOrderWithResult(ReturnOrderDTO returnOrderDTO ) throws ServiceException;

    /**
     * 获取退单信息 根据供货商编号和状态
     * @param supplierId
     * @param status
     * @return
     * @throws ServiceException
     */
    public List<ReturnOrderDTO> getReturnOrderBySupplierIdAndOrderStatus(String supplierId,String status ) throws ServiceException;


    /**
     * 获取退单信息 根据供货商编号和状态
     * @param supplierId  供货商Id
     * @param status    订单状态
     * @param excStatus 异常状态
     * @return
     * @throws ServiceException
     */
    public List<ReturnOrderDTO> getReturnOrderBySupplierIdAndOrderStatusAndExcStatus(String supplierId,String status,String excStatus ) throws ServiceException;

    /**
     * 修改退单状态
     * @param statusMap 状态信息  ORDERID(UUID),STATUS
     * @throws ServiceException
     */
    public void updateReturnOrderStatus(Map<String,String> statusMap) throws ServiceException;


    /**
     * 更改订单信息
     * @param statusMap 比较全的信息
     *   <set>
    <if test="status != null"> STATUS = #{status}, </if>
    <if test="memo != null"> MEMO = #{memo}, </if>
    <if test="updateTime != null"> UPDATE_TIME = #{updateTime}, </if>
    <if test="deliveryNo != null"> DELIVERY_NO = #{deliveryNo},</if>
    <if test="excState != null"> EXC_STATE = #{excState}, </if>
    <if test="excDesc != null"> EXC_DESC = #{excDesc}, </if>
    <if test="excTime != null"> EXC_TIME = #{excTime}</if>



    </set>
    where UUID = #{uuid}
     *
     * @throws ServiceException
     */
    public void updateReturnOrderMsg(Map<String,String> statusMap) throws ServiceException;
}
