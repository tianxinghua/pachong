package com.shangpin.ep.order.module.orderapiservice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.module.orderapiservice.impl.atelier.AtelierOrderHandler;

@Component("viettiServiceImpl")
public class ViettiServiceImpl extends AtelierOrderHandler {
	
	@Autowired
	private SupplierProperties properties;

	@Override
	public String getApiUrl() {
		return properties.getVietti().getUrl();
	}

	@Override
	public String getUserName() {
		return properties.getVietti().getUser();
	}

	@Override
	public String getPassword() {
		return properties.getVietti().getPassword();
	}

	@Override
	public String getGetItemStockInterface() {
		return properties.getVietti().getGetItemStockInterface();
	}

	@Override
	public String getCreateOrderInterface() {
		return properties.getVietti().getCreateOrderInterface();
	}

	@Override
	public String getSetStatusInterface() {
		return properties.getVietti().getSetStatusInterface();
	}
	
}
