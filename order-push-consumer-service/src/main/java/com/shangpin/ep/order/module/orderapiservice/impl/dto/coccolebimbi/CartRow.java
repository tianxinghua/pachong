package com.shangpin.ep.order.module.orderapiservice.impl.dto.coccolebimbi;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartRow {

	private String id;
	private String id_product;
	private String id_product_attribute;
	private String id_address_delivery;
	private String quantity;
}
