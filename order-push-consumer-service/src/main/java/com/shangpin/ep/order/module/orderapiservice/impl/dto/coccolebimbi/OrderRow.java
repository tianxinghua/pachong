package com.shangpin.ep.order.module.orderapiservice.impl.dto.coccolebimbi;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderRow {

	private String id;
	private String product_id;
	private String product_attribute_id ;
	private String product_name;
	private String product_quantity;
	private String product_reference;
	private String product_price;
	private String unit_price_tax_excl;
	private String unit_price_tax_incl;
}
