package com.shangpin.iog.coltorti.order;

import java.util.Map;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;

public class ColtortiOrderServiceImpl extends AbsOrderService{

	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		//推送订单
		
		
		
	}

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleEmail(OrderDTO orderDTO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getSupplierSkuId(Map<String, String> skuMap)
			throws ServiceException {
		// TODO Auto-generated method stub
		
	}

}