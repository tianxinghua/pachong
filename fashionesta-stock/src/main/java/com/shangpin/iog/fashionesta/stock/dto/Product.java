package com.shangpin.iog.fashionesta.stock.dto;

import java.util.ArrayList;
import java.util.List;

public class Product {
	private String productCode;//sku->spu:spuId
	private String productName;//name->sku:productName
	private String description;//description->sku:productDescription
	private String brand;//brand->spu:brandName
	private String category;//Im_type->spu:categoryName
	private String gender;//gender->spu:categoryGender
	private String color;//sku:color
	private String material;//spu:material

	private String[] image_url;//image->spu:picUrl
	private String made;//country_of_origin
	
	
	private List<Item> items = new ArrayList<Item>();
	
	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String[] getImage_url() {
		return image_url;
	}

	public void setImage_url(String[] image_url) {
		this.image_url = image_url;
	}

	public String getMade() {
		return made;
	}

	public void setMade(String made) {
		this.made = made;
	}	
	
}
