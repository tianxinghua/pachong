package com.shangpin.iog.theStyleSide.dto;

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
	private String description_ita;  //意大利描述（新增）
	private String description;
	private String material; //成分
	private String sesso; //
	private String category;
	private String season;
	private String italian_retail_price; //价格
	private String sconto;  //折扣（新增）
	private String pics;   //图片

	private String size_format; //
	private String size; //库存
	private String size_and_fit;
//	private String country;
	
}
