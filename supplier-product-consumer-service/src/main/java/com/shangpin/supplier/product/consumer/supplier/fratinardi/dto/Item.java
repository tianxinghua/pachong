package com.shangpin.supplier.product.consumer.supplier.fratinardi.dto;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {

	String category;
	@SerializedName("sku No")
	String sku_No;
	String Gender;
	String Barcode;
	@SerializedName("product No")
	String product_No;
	String name;
	String description;
	String materials;
	String color;
	String brand;
	String size;
	String qty;
	@SerializedName("market price")
	String market_price;
	String image1;
	String image2;
	String image3;
	String image4;
	private String season;
}