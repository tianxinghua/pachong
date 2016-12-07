package com.shangpin.supplier.product.message.original.conf.message.geb;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title:Item.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月6日 下午12:09:53
 */
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
	private Suitable suitable;
	private List<Material> technical_info;
	private String price;
	private String currency;
	private ItemImages item_images;
	private String retail_price;
}
