package com.shangpin.ep.order.module.orderapiservice.impl.dto.coccolebimbi;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Order {
	private String id;
	private String id_address_delivery;
	private String id_address_invoice;
	private String id_cart;
	private String id_currency;
	private String id_lang;
	private String id_customer;
	private String id_carrier;
	private String module;
	private String payment;
	private String secure_key;
	private String total_paid;
	private String total_paid_real;
	private String total_products;
	private String total_products_wt;
	private String conversion_rate;
	private String valid;
	private String current_state;
	private String total_discounts;
	private String total_discounts_tax_incl;
	private String total_discounts_tax_excl;
	private String total_paid_tax_incl;
	private String total_paid_tax_excl;
	private String total_shipping;
	private String total_shipping_tax_incl;
	private String total_shipping_tax_excl;
	private Associations associations;

}
