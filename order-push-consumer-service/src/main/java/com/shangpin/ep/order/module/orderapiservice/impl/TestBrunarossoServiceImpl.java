package com.shangpin.ep.order.module.orderapiservice.impl;

import org.springframework.stereotype.Component;

import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;

@Component("testBrunarossoServiceImpl")
public class TestBrunarossoServiceImpl extends BrunarossoServiceImpl {

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		try {
			throw new Exception("状态码:500");
			
		} catch (Exception e) {
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			handleException.handleException(orderDTO,e);
			orderDTO.setLogContent("推送订单异常========= "+e.getMessage());
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		}
	}
		
}
