package com.shangpin.iog.antonacci.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name="product")
public class Product {

	String code;	
	String product_name;
	String description;
	String category;
	String product_brand;
	String product_detail;
	String url;
	String image;
	String market_price;
	String sell_price;
	String brand;	
	String season;
	String color;
	String material;
	String gender;
	Other_images other_images;
	Sizes sizes;
}
