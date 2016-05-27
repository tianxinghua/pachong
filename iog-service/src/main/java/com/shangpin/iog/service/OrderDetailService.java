package com.shangpin.iog.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.OrderDetailDTO;

public interface OrderDetailService {
	/**
	 * 保存OrderDetailDTO
	 * @param orderDetailDTO
	 * @throws ServiceException
	 */
	public void saveOrderDetail(OrderDetailDTO orderDetailDTO)throws ServiceException;
	/**
	 * 更新子订单信息
	 * @param orderDetailDTO
	 * @throws ServiceException
	 */
	public void updateOrderDetail(OrderDetailDTO orderDetailDTO)throws ServiceException;
	/**
	 * 根据主订单号获取子订单dto
	 * @param masterOrderNo
	 * @return List<OrderDetailDTO> 子订单列表
	 * @throws ServiceException
	 */
	public List<OrderDetailDTO> getOrderDetailByMorderNo(String masterOrderNo)throws ServiceException;
	
	   /**
     * 更新订单信息
     * @param orderDTO
     * @throws ServiceException
     */
    public void update(OrderDetailDTO orderDetailDTO) throws ServiceException;
    	
    /**
     * 获取订单状态 根据供货商ID 和 订单状态
     * @param supplierId 供货商ID
     * @param status 状态码
     * @return
     * @throws ServiceException
     */
    public List<String> getOrderIdBySupplierIdAndOrderStatus(String supplierId,String status) throws ServiceException;
    
    /**
     * 判断时间是否超过一天
     * @param supplierId
     * @param status
     * @param date
     * @return
     * @throws ServiceException
     */
    public List<OrderDetailDTO>  getOrderBySupplierIdAndOrderStatus(String supplierId,String status,String date) throws ServiceException;
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
    public List<OrderDetailDTO>  getOrderBySupplierIdAndOrderStatusAndExceptionStatus(String supplierId,String status,String excState ,String date,int interval) throws ServiceException;

    /**
     * 根据订单的创建时间获取订单   根据订单ID,订单状态,开始时间,结束时间
     * @param supplierId
     * @param status
     * @param startTime
     * @param endTime
     * @return
     * @throws ServiceException
     */
    public List<OrderDetailDTO>  getOrderBySupplierIdAndOrderStatusAndTime(String supplierId,String status,String startTime,String endTime) throws ServiceException;

    /**
     *  根据更新状态的时间 获取订单   根据订单ID,订单状态,开始时间,结束时间
      * @param supplierId
     * @param status
     * @param startTime
     * @param endTime
     * @return
     * @throws ServiceException
     */
    public List<OrderDetailDTO>  getOrderBySupplierIdAndOrderStatusAndUpdateTime(String supplierId,String status,String startTime,String endTime) throws ServiceException;
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
			Date endDate,Integer pageIndex, Integer pageSize);
    
    /**
     * 查询订单，不分页
     * @param supplier
     * @param startDate
     * @param endDate
     * @return
     */
    public List<OrderDetailDTO> getOrderBySupplierIdAndTime(String supplier, Date startDate, Date endDate);
    /**
     * 获取异常订单信息
     * @return
     * @throws ServiceException
     */
    public List<OrderDetailDTO> getExceptionOrder() throws ServiceException;
    
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

}
