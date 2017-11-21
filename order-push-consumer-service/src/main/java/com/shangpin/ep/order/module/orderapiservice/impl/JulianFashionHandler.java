package com.shangpin.ep.order.module.orderapiservice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.module.orderapiservice.impl.atelier.AtelierOrderHandler;

@Component("julianFashionHandler")
public class JulianFashionHandler extends AtelierOrderHandler {
	
	@Autowired
	private SupplierProperties properties;

	@Override
	public String getApiUrl() {
		return properties.getJulianFashion().getUrl();
	}

	@Override
	public String getUserName() {
		return properties.getJulianFashion().getUser();
	}

	@Override
	public String getPassword() {
		return properties.getJulianFashion().getPassword();
	}

	@Override
	public String getGetItemStockInterface() {
		return properties.getJulianFashion().getGetItemStockInterface();
	}

	@Override
	public String getCreateOrderInterface() {
		return properties.getJulianFashion().getCreateOrderInterface();
	}

	@Override
	public String getSetStatusInterface() {
		return properties.getJulianFashion().getSetStatusInterface();
	}

}
