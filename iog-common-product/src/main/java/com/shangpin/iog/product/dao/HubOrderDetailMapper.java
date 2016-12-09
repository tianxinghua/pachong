package com.shangpin.iog.product.dao;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.HubOrderDetail;
import com.shangpin.iog.dto.OrderDetailDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface HubOrderDetailMapper extends IBaseDao<HubOrderDetail> {





	/**
	 * 通过采购单号查询子订单
	 * 
	 * @param spPurchaseNo
	 * @return 子订单
	 */
	public HubOrderDetail findSubOrderByPurchaseNo(
			@Param("purchaseNo") String spPurchaseNo);

	/**
	 * 通过自定义号查询子订单
	 * 
	 * @param   orderNo
	 * @return 子订单 OrderDetailDTO
	 */
	public HubOrderDetail findSubOrderByOrderNoAndSupplierId(@Param("orderNo") String orderNo, @Param("supplierId") String supplierId);

	/**
	 * 根据订单的子订单号查找子订单
	 * @param orderDetailNo
	 * @return
     */
	public List<HubOrderDetail> findOrderDetailByOrderDetailNo(@Param("orderDetailNo") String orderDetailNo);





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
	public List<HubOrderDetail> findBySupplierIdAndOrderStatusAndDateAndExcSatus(
			@Param("supplierId") String supplierId,
			@Param("status") String status, @Param("excState") String excState,
			@Param("date") String date, @Param("interval") int interval);
	/**
	 * 通过status,supplierid查询
	 * @param supplierId
	 * @param status
	 * @return
	 */
	public List<HubOrderDetail> findBySupplierIdAndStatus(
			@Param("supplierId") String supplierId,
			@Param("status") String status);
	/**
	 * 通过status supplierid date 查询未超过12h的
	 * @param supplierId
	 * @param status
	 * @param date
	 * @return
	 */
	public List<HubOrderDetail> findBySupplierIdAndStatusAndDate(
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
    public List<HubOrderDetail> findBySupplierIdAndStatusAndTime(@Param("supplierId") String supplierId,
																 @Param("status") String status, @Param("startTime") String startTime, @Param("endTime") String endTime) ;

    /**
     * 根据订单更新时间查询订单
     * @param supplierId
     * @param status
     * @param startTime
     * @param endTime
     * @return
     */
    public List<HubOrderDetail> findBySupplierIdAndStatusAndUpdateTime(@Param("supplierId") String supplierId,
																	   @Param("status") String status, @Param("startTime") String startTime, @Param("endTime") String endTime) ;

    public List<HubOrderDetail> getOrderBySupplierIdAndTime(@Param("supplierId") String supplier, @Param("startDate") Date startDate,
															@Param("endDate") Date endDate, @Param("CGD") String CGD, @Param("spSkuId") String spSkuId,
															@Param("supplierSkuId") String supplierSkuId, @Param("status") String status, RowBounds rowBounds);
    
    public List<HubOrderDetail> getOrderBySupplierIdAndTime(@Param("supplierId") String supplier, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
    /**
     * 获取异常订单信息
     * @return
     */
    public List<HubOrderDetail> findExceptionOrder() ;





	public int getOrderTotalBySupplierIdAndTime(@Param("supplierId") String supplier, @Param("startDate") String startTime, @Param("endDate") String endTime, @Param("CGD") String CGD,
												@Param("spSkuId") String spSkuId, @Param("supplierSkuId") String supplierSkuId, @Param("status") String status);

	public int getOrderTotalBySpPurchaseNo(@Param("supplierId") String supplierId,
										   @Param("startTime") String startTime, @Param("endTime") String endTime);
	
	/**
	 * 根据供应商门户编号，以及更新时间查找状态为nohandle、refunded、SHpurExp的订单
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<HubOrderDetail> findPushFailOrdersByUpdateTime(@Param("supplierId") String supplier, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
