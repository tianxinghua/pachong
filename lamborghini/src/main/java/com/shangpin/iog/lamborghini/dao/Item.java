package com.shangpin.iog.lamborghini.dao;

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
	private List<Material> technical_info;
//	private String suitable;
	private String made_in;
	private String quantity;
	private String season_year;
	private String season_reference;


	private Item_images item_images;
	private String color;
	private String size;

//	private String category;
	private String price;
	private String base_price;
	private String currency;
//	private String details_link;

}
