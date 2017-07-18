package com.shangpin.ep.order.module.orderapiservice.impl.dto.forzieri;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShippingAddress {
	private String shipping_address;
	private String full_name;
	private String address_line1;
	private String address_line2;
	private String locality;
	private String region;
	private String postal_code;
	private String country;
	private String phone_number;
}
