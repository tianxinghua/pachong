package com.shangpin.asynchronous.task.consumer.productimport.pending.spu.dao;

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
public class HubPendingSpuImportDTO {
	private String spPicUrl;
	private String productInfoUrl;
	private String supplierId;
	private String supplierNo;
	private String supplierName;
	private String supplierSpuNo;
	private String supplierCategoryname;
	private String hubCategoryName;
	private String hubCategoryNo;
	private String supplierBrandName;
	private String hubBrandNo;
	private String hubBrandName;
	private String supplierSpuModel;
	private String spuModel;
	private String seasonYear;
	private String seasonName;
	private String hubGender;
	private String spuName;
	private String supplierSpuColor;
	private String hubColor;
	private String supplierMaterial;
	private String hubMaterial;
	private String hubOrigin;
	private String spuDesc;
	private String memo;
	private String spuState;
	private String specificationType;
	private String totalStock;
	private String updateUser;
	private String filter;
	private String reason1;
	private String reason2;
	private String reason3;
	private String reason4;
	private String picRetry;
}
