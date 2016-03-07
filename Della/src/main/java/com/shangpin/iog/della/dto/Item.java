package com.shangpin.iog.della.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {

	String season;
	String brand_name;//品牌
	String brand_line;//品类
	String item_code;//sku
	String supplier_item_code;//spu
	String item_description;
	String color_code;
	String color_description;
	String size;
	String retail_price;//零售价
	String your_price;//
	String sold_price;//供货商价格
	String quantity;
	String size_family;
	String photo_links;
	String item_detailed_info;
	String gender; //性别
	String made_in;
	
}
