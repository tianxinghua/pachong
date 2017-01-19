package com.shangpin.supplier.product.consumer.supplier.monnierfreres.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
public class Product {
	private String product_id;
	private String name;
	private String description;
	private String brand;
	private String type;
	private String subCategory;
	private String gender;
	private String color;
	private String material;
	private String manufacturer;
	private String image_url_1;
	private String image_url_2;
	private String image_url_3;
	private String image_url_4;
	private String image_url_5;
	
	private String sku;//sku->skuid
	private String size;//size->sku:size
	private String qty;//qty->stock
	private String price_before_discount;//sku
	private String special_price;//sku
	private String ean13;
	private String price_type;
}
