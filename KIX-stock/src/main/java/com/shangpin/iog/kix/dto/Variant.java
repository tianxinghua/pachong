package com.shangpin.iog.kix.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Variant {
	private String id;
	private String product_id;
	private String title;//size
	private String price;
	private String sku;
	private String inventory_quantity;
}
