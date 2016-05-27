package com.shangpin.iog.product.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.OrderDetailDTO;
import com.shangpin.iog.product.dao.OrderDetailMapper;
import com.shangpin.iog.service.OrderDetailService;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
	final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final static String UPDATE_ERROR = "更新订单状态失败";
	private final static String UPDATE_EXCEPTON_MSG_ERROR = "更新订单异常信息时失败";

	@Autowired
	OrderDetailMapper orderDetailDao;

	@Override
	public void saveOrderDetail(OrderDetailDTO orderDetailDTO)
			throws ServiceException {
		orderDetailDao.saveOrderDetailDTO(orderDetailDTO);
	}

	@Override
	public void updateOrderDetail(OrderDetailDTO orderDetailDTO)
			throws ServiceException {
		orderDetailDao.updateByOrderNo(orderDetailDTO);
	}

	@Override
	public List<OrderDetailDTO> getOrderDetailByMorderNo(String masterOrderNo)
			throws ServiceException {
		return orderDetailDao.findSubOrderListByMOrderNo(masterOrderNo);
	}

	@Override
	public void update(OrderDetailDTO orderDetailDTO) throws ServiceException {
		orderDetailDao.updateByOrderNo(orderDetailDTO);
	}

	@Override
	public List<OrderDetailDTO> getOrderDetailBySupplierIdAndOrderStatus(String supplierId,
			String status) throws ServiceException {
	
		return orderDetailDao.findBySupplierIdAndStatus(supplierId, status);
	}

	@Override
	public List<OrderDetailDTO> getOrderBySupplierIdAndOrderStatus(
			String supplierId, String status, String date)
			throws ServiceException {
		return orderDetailDao.findBySupplierIdAndStatusAndDate(supplierId,
				status, date);
	}

	@Override
	public List<OrderDetailDTO> getOrderBySupplierIdAndOrderStatusAndExceptionStatus(
			String supplierId, String status, String excState, String date,
			int interval) throws ServiceException {
		return orderDetailDao.findBySupplierIdAndOrderStatusAndDateAndExcSatus(
				supplierId, status, excState, date, interval);

	}

	@Override
	public List<OrderDetailDTO> getOrderBySupplierIdAndOrderStatusAndTime(
			String supplierId, String status, String startTime, String endTime)
			throws ServiceException {
		return orderDetailDao.findBySupplierIdAndStatusAndTime(supplierId,
				status, startTime, endTime);
	}

	@Override
	public List<OrderDetailDTO> getOrderBySupplierIdAndOrderStatusAndUpdateTime(
			String supplierId, String status, String startTime, String endTime)
			throws ServiceException {
		return orderDetailDao.findBySupplierIdAndStatusAndUpdateTime(
				supplierId, status, startTime, endTime);
	}

	@Override
	public List<OrderDetailDTO> getOrderBySupplierIdAndTime(String supplier,
			Date startDate, Date endDate, Integer pageIndex, Integer pageSize) {

		return orderDetailDao.getOrderBySupplierIdAndTime(supplier, startDate,
				endDate, new RowBounds(pageIndex, pageSize));
	}

	@Override
	public List<OrderDetailDTO> getOrderBySupplierIdAndTime(String supplier,
			Date startDate, Date endDate) {
		return orderDetailDao.getOrderBySupplierIdAndTime(supplier, startDate,
				endDate);
	}

	@Override
	public List<OrderDetailDTO> getExceptionOrder() throws ServiceException {
		return orderDetailDao.findExceptionOrder();
	}

	private void judgeMapParam(Map<String, String> exceptionMap)
			throws ServiceMessageException {
		if (null == exceptionMap || exceptionMap.size() == 0)
			throw new ServiceMessageException("参数传入错误");

	}

	@Override
	public void updateOrderStatus(Map<String, String> statusMap)
			throws ServiceException {
		judgeMapParam(statusMap);
		try {
			orderDetailDao.updateOrderStatus(statusMap);
		} catch (Exception e) {
			logger.error(UPDATE_ERROR);
			e.printStackTrace();
		}

	}

	@Override
	public void updateExceptionMsg(Map<String, String> exceptionMap)
			throws ServiceException {
		judgeMapParam(exceptionMap);
		try {
			orderDetailDao.updateOrderExceptionMsg(exceptionMap);
		} catch (Exception e) {
			logger.error(UPDATE_EXCEPTON_MSG_ERROR);
			e.printStackTrace();
		}
	}

	@Override
	public void updateDeliveryNo(Map<String, String> exceptionMap)
			throws ServiceException {
		judgeMapParam(exceptionMap);

		try {
			orderDetailDao.updateDeliveryNo(exceptionMap);
		} catch (Exception e) {
			logger.error(exceptionMap.get("uuid") + "更改发货单失败");
			e.printStackTrace();
		}
	}

	@Override
	public void updateDetailMsg(Map<String, String> detailMap)
			throws ServiceException {
		judgeMapParam(detailMap);
		orderDetailDao.updateAllByMap(detailMap);
	}

}
