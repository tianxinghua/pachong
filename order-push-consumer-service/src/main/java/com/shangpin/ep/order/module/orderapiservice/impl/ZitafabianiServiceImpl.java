package com.shangpin.ep.order.module.orderapiservice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.module.orderapiservice.impl.atelier.AtelierOrderHandler;

@Component("zitafabianiServiceImpl")
public class ZitafabianiServiceImpl extends AtelierOrderHandler {
	
	@Autowired
	private SupplierProperties properties;

	@Override
	public String getApiUrl() {
		return properties.getZitafabiani().getUrl();
	}

	@Override
	public String getUserName() {
		return properties.getZitafabiani().getUser();
	}

	@Override
	public String getPassword() {
		return properties.getZitafabiani().getPassword();
	}

	@Override
	public String getGetItemStockInterface() {
		return properties.getZitafabiani().getGetItemStockInterface();
	}

	@Override
	public String getCreateOrderInterface() {
		return properties.getZitafabiani().getCreateOrderInterface();
	}

	@Override
	public String getSetStatusInterface() {
		return properties.getZitafabiani().getSetStatusInterface();
	}
	
}
