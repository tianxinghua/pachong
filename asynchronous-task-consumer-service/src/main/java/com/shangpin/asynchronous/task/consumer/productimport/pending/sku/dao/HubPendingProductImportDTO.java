package com.shangpin.asynchronous.task.consumer.productimport.pending.sku.dao;

import java.math.BigDecimal;

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
	private String supplierId;
	private String supplierNo;
	private String supplierName;
	private String supplierSpuNo;
	private String categoryName;
	private String hubCategoryNo;
	private String hubBrandNo;
	private String brandName;
	private String spuModel;
	private String seasonYear;
	private String seasonName;
	private String hubGender;
	private String supplierSkuNo;
	private String skuName;
	private String supplierBarcode;
	private String hubColor;
	private String specificationType;
	private String sizeType;
	private String hubSkuSize;
	private String hubMaterial;
	private String hubOrigin;
	private String supplyPrice;
	private String supplyPriceCurrency;
	private String marketPrice;
	private String marketPriceCurrencyorg;
	private String measurement;
	private String spuDesc;
	public String[] getHubProductTemplate() {
		String [] temp = {"supplierId","supplierNo","supplierName","supplierSpuNo","categoryName","hubCategoryNo","hubBrandNo","brandName","spuModel","seasonYear","seasonName","hubGender","supplierSkuNo","skuName","supplierBarcode",
				"hubColor","specificationType","sizeType","hubSkuSize","hubMaterial","hubOrigin","supplyPrice","supplyPriceCurrency","marketPrice","marketPriceCurrencyorg","measurement","spuDesc"};
		return temp;
	}

}
