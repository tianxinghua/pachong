package com.shangpin.ephub.product.business.ui.airshop.product.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductDetailResponseDTO {

	private String productCode;
	private String colour;
	private String material;
	private String type;//1save,2submmit
	private String addOrupdate;// add update
	private String category;
	private String brand;
	private Long supplierSpuId;
	private String spuName;
	private String marketPrice;
	private String supplyPrice;
	private String currency;
	/**
	 * 	性别：0未选择 1男 2女 3中性
	 */
	private String gender;
	private String year;
	private String season;
	private String madeIn;
	private String description;
	private List<String> images;

	private List<ProductDetailSkuResponseDTO> skus;
}
