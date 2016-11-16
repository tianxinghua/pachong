package com.shangpin.iog.product.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.OrderDetailDTO;

@Mapper
public interface OrderDetailMapper extends IBaseDao<OrderDetailDTO> {

	public void saveOrderDetailDTO(OrderDetailDTO orderDetailDTO);

	/**
	 * 通过自定义订单号更新
	 * 
	 * @param orderDetailDTO
	 */
	public void updateByOrderNo(OrderDetailDTO orderDetailDTO);

	/**
	 * 通过主订单号查询子订单列表
	 * 
	 * @param masterOrderNo
	 * @return 子订单列表
	 */
	public List<OrderDetailDTO> findSubOrderListByMOrderNo(
			@Param("spMorderNo") String masterOrderNo);

	/**
	 * 通过采购单号查询子订单
	 * 
	 * @param spPurchaseNo
	 * @return 子订单
	 */
	public OrderDetailDTO findSubOrderByPurchaseNo(
			@Param("purchaseNo") String spPurchaseNo);

	/**
	 * 通过自定义号查询子订单
	 * 
	 * @param   orderNo
	 * @return 子订单 OrderDetailDTO
	 */
	public OrderDetailDTO findSubOrderByOrderNoAndSupplierId(@Param("orderNo") String orderNo,@Param("supplierId") String supplierId);

	/**
	 * 根据订单的子订单号查找子订单
	 * @param orderDetailNo
	 * @return
     */
	public List<OrderDetailDTO> findOrderDetailByOrderDetailNo(@Param("orderDetailNo") String orderDetailNo);

	/**
	 * 根据订单唯一编号获取信息
	 * 
	 * @param uuid
	 * 
	 * @return OrderDetailDTO
	 */
	public OrderDetailDTO findByUuId(@Param("uuid") String uuid);

	/**
	 * 通过supplierId,SupplierNo,status查询子订单
	 * 
	 * @param supplierId
	 * @param supplierNo
	 * @param status
	 * @return OrderDetailDTO
	 */
	public OrderDetailDTO findBySIDAndSNOAndStatus(
			@Param("supplierId") String supplierId,
			@Param("supplierNo") String supplierNo,
			@Param("status") String status);

	/**
	 * 查询供货商异常的待处理订单
	 * 
	 * @param supplierId

	 * @param status
	 * @param excState
	 * @param date
	 * @param interval
	 * @return OrderDetailDTO
	 */
	public List<OrderDetailDTO> findBySupplierIdAndOrderStatusAndDateAndExcSatus(
			@Param("supplierId") String supplierId,
			@Param("status") String status, @Param("excState") String excState,
			@Param("date") String date, @Param("interval") int interval);
	/**
	 * 通过status,supplierid查询
	 * @param supplierId
	 * @param status
	 * @return
	 */
	public List<OrderDetailDTO> findBySupplierIdAndStatus(
			@Param("supplierId") String supplierId,
			@Param("status") String status);
	/**
	 * 通过status supplierid date 查询未超过12h的
	 * @param supplierId
	 * @param status
	 * @param date
	 * @return
	 */
	public List<OrderDetailDTO> findBySupplierIdAndStatusAndDate(
			@Param("supplierId") String supplierId,
			@Param("status") String status, @Param("date") String date);
    /**
     * 查询订单
     * @param supplierId
     * @param status 订单状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public List<OrderDetailDTO> findBySupplierIdAndStatusAndTime(@Param("supplierId") String supplierId,
            @Param("status") String status,@Param("startTime")String startTime,@Param("endTime")String endTime) ;

    /**
     * 根据订单更新时间查询订单
     * @param supplierId
     * @param status
     * @param startTime
     * @param endTime
     * @return
     */
    public List<OrderDetailDTO> findBySupplierIdAndStatusAndUpdateTime(@Param("supplierId") String supplierId,
            @Param("status") String status,@Param("startTime")String startTime,@Param("endTime")String endTime) ;

    public List<OrderDetailDTO> getOrderBySupplierIdAndTime(@Param("supplierId")String supplier, @Param("startDate")Date startDate,
    		@Param("endDate")Date endDate,@Param("CGD")String CGD,@Param("spSkuId")String spSkuId,
    		@Param("supplierSkuId")String supplierSkuId,@Param("status")String status,RowBounds rowBounds);
    
    public List<OrderDetailDTO> getOrderBySupplierIdAndTime(@Param("supplierId")String supplier, @Param("startDate")Date startDate, @Param("endDate")Date endDate);
    /**
     * 获取异常订单信息
     * @return
     */
    public List<OrderDetailDTO> findExceptionOrder() ;
    /**
     * 更新订单状态
     * @param paraMap
     * @return
     */
    public int updateOrderStatus(Map<String,String> paraMap);

    /**
     * 增加异常信息
     * @param paraMap
     * @return
     */
    public int updateOrderExceptionMsg(Map<String,String> paraMap);

    /**
     * 更新订单发货单信息
     * map 包含
     * @param paraMap
     * @return
     */
    public int updateDeliveryNo(Map<String,String> paraMap);
    /**
     * 通过uuid更新详细订单信息
     * map 包含status  orderNo updateTime spPurchaseDetailNo spPurchaseNo deliveryNo excState excDesc purchasePriceDetail excTime
     * @param paraMap
     * @return
     */
    public int updateAllByMap(Map<String,String> paraMap);
    /**
     * 通过epMasterOrderNo查询
     * @param epMasterOrderNo
     * @return
     */
    public List<OrderDetailDTO> getDetailDTOByEpMasterOrderNo(@Param("epMasterOrderNo") String epMasterOrderNo);

	public int getOrderTotalBySupplierIdAndTime(@Param("supplierId") String supplier,@Param("startDate") String startTime,@Param("endDate") String endTime,@Param("CGD") String CGD,
					@Param("spSkuId") String spSkuId,@Param("supplierSkuId") String supplierSkuId,@Param("status") String status);

	public int getOrderTotalBySpPurchaseNo(@Param("supplierId") String supplierId,
            @Param("startTime")String startTime,@Param("endTime")String endTime);
}
