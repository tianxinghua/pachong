package com.shangpin.iog.efashion.dao;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CopyOfItem {

	private String product_id;
	private String sku_id;
	private String brand;
	private String product_reference;
	private String color_reference;
	private String made_in;
	private String item_intro;
	private String item_description;
	private String technical_info;
	private String suitable;

	private String quantity;
	private String season_year;
	private String season_reference;


	private Item_images item_images;
	private String color;
	private String size;
	private String gender;
	private String category;
	private String price;
	private String price_IT;
	private String currency;
	private String details_link;

}
