package com.shangpin.ep.order.module.orderapiservice.impl;

import com.shangpin.ep.order.conf.mail.message.ShangpinMail;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.module.orderapiservice.impl.atelier.AtelierOrderHandler;

@Component("mengottiServiceImpl")
public class MengottiServiceImpl extends AtelierOrderHandler {
	
	@Autowired
	private SupplierProperties properties;

	@Override
	public String getApiUrl() {
		return properties.getMengotti().getUrl();
	}

	@Override
	public String getUserName() {
		return properties.getMengotti().getUser();
	}

	@Override
	public String getPassword() {
		return properties.getMengotti().getPassword();
	}

	@Override
	public String getGetItemStockInterface() {
		return properties.getMengotti().getGetItemStockInterface();
	}

	@Override
	public String getCreateOrderInterface() {
		return properties.getMengotti().getCreateOrderInterface();
	}

	@Override
	public String getSetStatusInterface() {
		return properties.getMengotti().getSetStatusInterface();
	}

	@Override
	public ShangpinMail getEmailMessage() {
		return null;
	}

	@Override
	public String getEmailContent(OrderDTO orderDTO, String size) {
		return null;
	}

}
