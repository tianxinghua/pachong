package com.shangpin.iog.service;

import java.util.List;

import com.shangpin.framework.ServiceException;
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
	
	
}
