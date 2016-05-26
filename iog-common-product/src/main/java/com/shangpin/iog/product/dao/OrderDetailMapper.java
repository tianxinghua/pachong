package com.shangpin.iog.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.OrderDetailDTO;

@Mapper
public interface OrderDetailMapper extends IBaseDao<OrderDTO>{
	
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
	public List<OrderDetailDTO> findSubOrderListByMOrderNo(@Param("spMorderNo") String masterOrderNo);
	/**
	 * 通过采购单号查询子订单
	 * 
	 * @param spPurchaseNo
	 * @return 子订单
	 */
	public OrderDetailDTO findSubOrderByPurchaseNo(@Param("purchaseNo") String spPurchaseNo);

	/**
	 * 通过自定义号查询子订单
	 * 
	 * @param String
	 *            orderNo
	 * @return 子订单 OrderDetailDTO
	 */
	public OrderDetailDTO findSubOrderByOrderNo(@Param("orderNo") String orderNo);
	/**
	 * 根据订单唯一编号获取信息
	 * 
	 * @param uuId
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
	 * @param supplierNo
	 * @param status
	 * @param excState
	 * @param date
	 * @param interval
	 * @return OrderDetailDTO
	 */
	public List<OrderDetailDTO> findBySupplierIdAndOrderStatusAndDateAndExcSatus(
			@Param("supplierId") String supplierId,
			@Param("supplierNo") String supplierNo,
			@Param("status") String status, @Param("excState") String excState,
			@Param("date") String date, @Param("interval") int interval);
	
}
