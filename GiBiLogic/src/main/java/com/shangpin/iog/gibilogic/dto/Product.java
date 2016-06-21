package com.shangpin.iog.gibilogic.dto;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Product {
	private String virtuemart_product_id;
	private String product_desc;
	private String material;
	private String product_sku;
	private String virtuemart_category_id;
	private String product_name;
	private String vendor_name;
	private String product_price;
	private String product_in_stock;
	private String image_url;
	private String Colore;
	private String Stagione;
	@SerializedName("Made in")
	private String Madein;
	private String Size;
	private String gender;
}
