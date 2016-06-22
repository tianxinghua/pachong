package com.shangpin.iog.generater.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
public class ProductDTO {
	//size and stock 
	private String sizeandstock;

	private String spuId;
	private String skuId;
	private String gender;
	private String category;
	private String brand;
	private String season;
	private String picurl;
	private String origin;
	private String material;
	private String productName;
	private String marketPrice;
	private String salePrice;
	private String supplierPrice;
	private String barcode;
	private String color;
	private String currency;
	private String size;
	private String stock;
	private String productCode;
	private String description;
	//XML 解析使用
	private List<ProductDTO> productList;

	
}
