package com.shangpin.supplier.product.consumer.supplier.forzieri.dto;

import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Product {
	//[id, title, description, google_product_category, product_type, link, image_link, additional_image_link, condition, availability, availability_date, 
	//price, sale_price, sale_price_effective_date, gtin, mpn, brand, identifier exists, gender, age_group, color, size, size_type, material, pattern, 
	//item_group_id, shipping(price), shipping_weight, adwords_grouping,
	//adwords_labels, adwords_redirect, excluded_destination, expiration_date]
	private String id;
	private String title;
	private String description;
	private String google_product_category;
	private String product_type;
	private String link;
	private String image_link;
	private String additional_image_link;
	private String condition;
	private String availability;
	private String availability_date;
	private String price;
	private String sale_price;
	private String sale_price_effective_date;
	private String gtin;
	private String mpn;
	private String brand;
	@Value("identifier exists")
	private String identifierExists;
	private String gender;
	private String age_group;
	private String color;
	private String size;
	private String size_type;
	private String material;
	private String pattern;
	private String item_group_id;
	private String shipping;
	private String shipping_weight;
	private String adwords_grouping;
	private String adwords_labels;
	private String adwords_redirect;
	private String excluded_destination;
	private String expiration_date;
	
}
