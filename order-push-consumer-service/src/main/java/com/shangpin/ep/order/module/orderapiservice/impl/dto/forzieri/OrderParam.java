package com.shangpin.ep.order.module.orderapiservice.impl.dto.forzieri;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderParam {
	
	private Shopper shopper;
	private ShippingAddress shipping_address;
	private BillingAddress billing_address;
	private List<Item> items;
	private String merchant_reference;
}
