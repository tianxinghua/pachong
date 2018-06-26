package com.shangpin.supplier.product.consumer.supplier.deliberti2.dto;

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
}
