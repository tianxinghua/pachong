package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.OrderDetailDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface HubOrderDetailService {

    /**
     * 根据尚品的子订单号获取订单明细
     * @param orderDetailNo
     * @return
     * @throws ServiceException
     */
    public List<OrderDetailDTO> getOrderDetailBySpOrderDetailNo(String orderDetailNo)  throws ServiceException;


    /**
     * 获取订单状态 根据供货商ID 和 订单状态
     * @param supplierId 供货商ID
     * @param status 状态码
     * @return
     * @throws ServiceException
     */
    public List<OrderDetailDTO> getOrderDetailBySupplierIdAndOrderStatus(String supplierId, String status) throws ServiceException;
    
    /**
     * 判断时间是否超过一天
     * @param supplierId
     * @param status
     * @param date
     * @return
     * @throws ServiceException
     */
    public List<OrderDetailDTO>  getOrderBySupplierIdAndOrderStatus(String supplierId, String status, String date) throws ServiceException;
    /**
     * 查询异常的订单
     * @param supplierId   ：供货商ID
     * @param status   : 订单状态
     * @param excState  ：异常状态
     * @param date  ：需要的处理时间
     * @param interval  :时间间隔
     * @return
     * @throws ServiceException
     */
    public List<OrderDetailDTO>  getOrderBySupplierIdAndOrderStatusAndExceptionStatus(String supplierId, String status, String excState, String date, int interval) throws ServiceException;

    /**
     * 根据订单的创建时间获取订单   根据订单ID,订单状态,开始时间,结束时间
     * @param supplierId
     * @param status
     * @param startTime
     * @param endTime
     * @return
     * @throws ServiceException
     */
    public List<OrderDetailDTO>  getOrderBySupplierIdAndOrderStatusAndTime(String supplierId, String status, String startTime, String endTime) throws ServiceException;

    /**
     *  根据更新状态的时间 获取订单   根据订单ID,订单状态,开始时间,结束时间
      * @param supplierId
     * @param status
     * @param startTime
     * @param endTime
     * @return
     * @throws ServiceException
     */
    public List<OrderDetailDTO>  getOrderBySupplierIdAndOrderStatusAndUpdateTime(String supplierId, String status, String startTime, String endTime) throws ServiceException;
    /**
     * 查询订单
     * @param supplier
     * @param startDate
     * @param endDate
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public List<OrderDetailDTO> getOrderBySupplierIdAndTime(String supplier, Date startDate,
                                                            Date endDate, String CGD, String spSkuId, String supplierSkuId, String status, Integer pageIndex, Integer pageSize);


    /**
     * 获取总数量
     * @param supplier
     * @param startTime
     * @param endTime
     * @param CGD
     * @param spSkuId
     * @param supplierSkuId
     * @param status
     * @return
     */
    public int getOrderTotalBySupplierIdAndTime(String supplier, String startTime,
                                                String endTime,String CGD,String spSkuId,String supplierSkuId,String status);
    /**
     * 查询订单，不分页
     * @param supplier
     * @param startDate
     * @param endDate
     * @return
     */
    public List<OrderDetailDTO> getOrderBySupplierIdAndTime(String supplier, Date startDate, Date endDate);

    



    /**
     * 通过epMasterOrderNo查询
     * @param epMasterOrderNo
     * @return List<OrderDetailDTO>
     * @throws ServiceException
     */
    public List<OrderDetailDTO> getDetailDTOByEpMasterOrderNo(String epMasterOrderNo) throws ServiceException;



}
