package com.shangpin.com.mcw.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class CsvDTO implements Serializable {
	private String barcode;
	private String brandName;
	private String skuId;
	private String productDescription;
	private String size;
	private String categoryName;
	private String categoryGender;
	private String subCategoryName;
	private String material;
	private String color;
	private String productOrigin;
	private String spuId;
	private String newSalePrice;
	private String stock;
	private String seasonName = "2018秋冬";
}
