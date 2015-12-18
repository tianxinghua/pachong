package com.shangpin.iog.stock;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {

	String category;
	String sku_No;
	String Gender;
	String Barcode;
	String product_No;
	String name;
	String description;
	String materials;
	String color;
	String brand;
	String size;
	String qty;
	String market_price;
	String image1;
	String image2;
	String image3;
	String image4;
	@Override
	public String toString() {
		return "Item [category=" + category + ", sku_No=" + sku_No
				+ ", Gender=" + Gender + ", Barcode=" + Barcode
				+ ", product_No=" + product_No + ", name=" + name
				+ ", description=" + description + ", materials=" + materials
				+ ", color=" + color + ", brand=" + brand + ", size=" + size
				+ ", qty=" + qty + ", market_price=" + market_price
				+ ", image1=" + image1 + ", image2=" + image2 + ", image3="
				+ image3 + ", image4=" + image4 + "]";
	}
	

}
