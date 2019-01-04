package com.shangpin.iog.monnalisa.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CsvDTO {
	/**
	 *id;title;description;google_product_category;
	 * link;image_link;additional_image_link;availability;price;sale_price;barcode;brand;adult;product_type;
	 * mobile_link;condition;item_group_id;color;gender;age_group;
	 * material;pattern;size;shipping;multipack;is_bundle;custom_label_0;season;country;anno
	 *
	 */
	private String id;
	private String title;
	private String description;
	private String google_product_category;
	private String link;
	private String image_link;
	private String additional_image_link;
	private String availability;
	private String price;
	private String sale_price;
	private String barcode;
	private String brand;
	private String adult;
	private String product_type;
	private String mobile_link;
	private String condition;
	private String item_group_id;
	private String color;
	private String gender;
	private String age_group;
	private String material;
	private String pattern;
	private String size;
	private String shipping;
	private String multipack;
	private String is_bundle;
	private String custom_label_0;
	private String season;
	private String country;
	private String anno;
	private int stock;


	
}
