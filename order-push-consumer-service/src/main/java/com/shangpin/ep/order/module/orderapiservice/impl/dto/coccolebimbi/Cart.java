package com.shangpin.ep.order.module.orderapiservice.impl.dto.coccolebimbi;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Cart {

	private String id;
	private String id_address_delivery;
	private String id_address_invoice;
	private String id_currency;
	private String id_customer;
	private String id_guest;
	private String id_lang;
	private String id_shop_group;
	private String id_shop;
	private String id_carrier;
	private String recyclable;
	private String gift;
	private String gift_message;
	private String mobile_theme;
	private String delivery_option;
	private String secure_key;
	private String allow_seperated_package;
	private String date_add;
	private String date_upd;
	private Associations associations;
}
