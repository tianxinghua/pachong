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
	@SerializedName("product_sku")
	private String skuId;
	private String product_parent_id;
	@SerializedName("virtuemart_category_id")
	private String category;
	private String product_name;
	private String vendor_name;
	private String product_price;
	@SerializedName("product_in_stock")
	private String stock;
	@SerializedName("image_url")
	private String image;
	private String Colore;
	@SerializedName("Stagione")
	private String season;
	@SerializedName("Made in")
	private String Madein;
	private String Size;
	private String gender;
	private String manufacturer;
}
