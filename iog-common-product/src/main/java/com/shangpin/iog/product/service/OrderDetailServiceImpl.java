package com.shangpin.iog.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.OrderDetailDTO;
import com.shangpin.iog.product.dao.OrderDetailMapper;
import com.shangpin.iog.service.OrderDetailService;
@Service
public class OrderDetailServiceImpl implements OrderDetailService{
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
		orderDetailDao.update(orderDetailDTO);
	}

	@Override
	public List<OrderDetailDTO> getOrderDetailByMorderNo(String masterOrderNo)
			throws ServiceException {
		return orderDetailDao.findSubOrderListByMOrderNo(masterOrderNo);
	}

}
