package com.shangpin.asynchronous.task.consumer.productimport.pending.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *@Author: zhaogenchun
 *@Date: 2016年12月15日 15:54:00
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubPendingProductImportDTO {
	private String supplierNo;
	private String categoryName;
	private String categoryNo;
	private String brandNo;
	private String brandName;
	private String productCode;
	private String seasonYear;
	private String seasonName;
	private String gender;
	private String supplierSkuNo;
	private String productName;
	private String barcode;
	private String color;
	private String specificationType;
	private String sizeType;
	private String size;
	private String material;
	private String madeIn;
	private String supplierPrice;
	private String supplierCurrency;
	private String marketPrice;
	private String marketCurrency;
	private String measure;
	private String description;

}
