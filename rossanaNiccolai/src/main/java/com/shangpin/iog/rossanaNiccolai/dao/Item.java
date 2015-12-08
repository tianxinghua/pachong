package com.shangpin.iog.rossanaNiccolai.dao;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Item {

	private String product_id;
	private String product_reference;
	private String color_reference;
	private String first_category;
	private String second_category;
	private String gender;
	private String brand;
	private String item_name;
	private String item_intro;
	private String item_description;
	private String color;
	private String size;
	private String quantity;
	private String season_year;
	private String season_reference;
//	private String suitable;
	private Material [] technical_info;
	private String price;
	private String price_IT;
	private String currency;
	private Image item_images;
}
