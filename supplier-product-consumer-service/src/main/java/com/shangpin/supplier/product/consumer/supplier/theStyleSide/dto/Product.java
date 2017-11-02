package com.shangpin.supplier.product.consumer.supplier.theStyleSide.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Product {

	// barcode 用sku+size 组装

		private String spu;//spu,  sku用spu+size
		private String barcode;
		private String brand;
		private String name;
		private String color;
		private String description;
		private String material;
		private String sesso;
		private String category;
		private String season;
		private String italian_retail_price;
		private String pics;
		private String size;
		private String stock;
//		private String country;
	
}
