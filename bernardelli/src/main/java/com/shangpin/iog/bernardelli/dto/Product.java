package com.shangpin.iog.bernardelli.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Product {
	private String product_id;
	private String brand_id;
	private String type;
	private String season;
	private String product_code;
	private String product_name;
	private String category;
	private String product_description;
	private String retail_price;
	private String product_material;
	private String product_colour;
	private String[] pictures;
	private Items items;
}
