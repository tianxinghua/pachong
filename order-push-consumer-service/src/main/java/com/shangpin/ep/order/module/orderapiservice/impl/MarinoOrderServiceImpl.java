package com.shangpin.ep.order.module.orderapiservice.impl;

import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.module.orderapiservice.impl.atelier.AtelierOrderHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("marinoOrderService")
public class MarinoOrderServiceImpl extends AtelierOrderHandler {
	
	@Autowired
	private SupplierProperties properties;

	@Override
	public String getApiUrl() {
		return properties.getMarino().getUrl();
	}

	@Override
	public String getUserName() {
		return properties.getMarino().getUser();
	}

	@Override
	public String getPassword() {
		return properties.getMarino().getPassword();
	}

	@Override
	public String getGetItemStockInterface() {
		return properties.getMarino().getGetItemStockInterface();
	}

	@Override
	public String getCreateOrderInterface() {
		return properties.getMarino().getCreateOrderInterface();
	}

	@Override
	public String getSetStatusInterface() {
		return properties.getMarino().getSetStatusInterface();
	}

}
