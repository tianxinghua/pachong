package com.shangpin.iog.deliberti.stock.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
	private String spuId;//spuid
	private String brand;
	private String season;
	private String gender;
	private String maincat;
	//private String MainCategoryDesc;
	private String subcat;
	//private String SubCategoryDesc;
	private String price;
	private String discountIfAny;//折扣
	private String productName;
	private String pictureUrl;
	private String gtin;//条形码
	private String material;//材质
	private String color;//颜色
	private String sizeRange;//尺寸范围
	private String sizeAndquantity;//需要拆分
	private String description;//描述
	private String colorCode;
	private String origin;

	@Override
	public String toString() {
		return "Product{" +
				"spuId='" + spuId + '\'' +
				", brand='" + brand + '\'' +
				", season='" + season + '\'' +
				", gender='" + gender + '\'' +
				", maincat='" + maincat + '\'' +
				", subcat='" + subcat + '\'' +
				", price='" + price + '\'' +
				", discountIfAny='" + discountIfAny + '\'' +
				", productName='" + productName + '\'' +
				", pictureUrl='" + pictureUrl + '\'' +
				", gtin='" + gtin + '\'' +
				", material='" + material + '\'' +
				", color='" + color + '\'' +
				", sizeRange='" + sizeRange + '\'' +
				", sizeAndquantity='" + sizeAndquantity + '\'' +
				", description='" + description + '\'' +
				", colorCode='" + colorCode + '\'' +
				", origin='" + origin + '\'' +
				'}';
	}
}
