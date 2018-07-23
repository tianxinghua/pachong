package com.shangpin.supplier.product.consumer.supplier.theStyleSide.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CsvDTO {

	// barcode 用sku+size 组装

		private String sku;//spu,  sku用spu+size
		private String product_code;
		private String brand;
		private String model_number;
		private String color;
		private String color_filter;
		private String description;
		private String material;
		private String sesso;
		private String category;
		private String season;
		private String italian_retail_price;
		private String pics;
		private String size_format;
		private String size;
		private String size_and_fit;
//		private String country;
	
}
