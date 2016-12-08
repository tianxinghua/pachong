package com.shangpin.supplier.product.consumer.supplier.common.atelier.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtelierSpu {

	private String spuId;//一般第0列
	private String seasonName;//一般第1列
	private String brandName;//一般2列
	private String styleCode;//一般3列
	private String colorCode;//一般4列
	private String categoryGender;//一般5列
	private String categoryName;//一般8列
	private String colorName;//一般10列
	private String material1;//一般11列
	private String description;//一般15列
	private String supplierPrice;//一般16列
	private String material3;//一般42列
	private String productOrigin;//一般40列
}
