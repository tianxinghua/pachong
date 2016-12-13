package com.shangpin.supplier.product.consumer.supplier.geb.dto;


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
	private String size;
	private String color;
	private String quantity;
	private String season_year;
	private String season_reference;
	private String made_in;
	private List<Measure> suitable;
	private List<Material> technical_info;
	private String price;
	private String currency;
	private Item_images item_images;
	private String retail_price;
}
