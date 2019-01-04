package com.shangpin.iog.prestashop.order;

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
	private String current_state;
	private String module;
	private String valid;
	
	private String invoice_number;
	private String invoice_date;
	private String delivery_number;
	private String delivery_date;
	
	
	private String date_add;
	private String date_upd;
	
	private String payment;
	private String secure_key;
	private String total_paid;
	private String total_paid_real;
	private String total_products;
	private String total_products_wt;
	private String conversion_rate;
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
