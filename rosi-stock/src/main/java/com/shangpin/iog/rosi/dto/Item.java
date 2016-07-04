package com.shangpin.iog.rosi.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name="item")
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {

	@javax.xml.bind.annotation.XmlElement(name="no-name")
	String no_name;
	@javax.xml.bind.annotation.XmlElement(name="Discounted-price")
	String Discounted_price;
	String category_name;
	String brand_name;
	String product_code;
	String supplier_SKU;
	String gender;
	String color;
	String composition;//材质
	String material;
	String origin;
	String season;
	String subtitle;
	String description;
	String image_link_1;
	String image_link_2;
	String image_link_3;
	String image_link_4;
	String image_link_5;
	String stock_sku;
	String Stock;
	String size;
	String price;
	String sku;
	
}
