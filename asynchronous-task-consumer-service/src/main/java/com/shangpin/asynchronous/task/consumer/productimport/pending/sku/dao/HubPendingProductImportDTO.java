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
	private String supplierNo;
	private String supplierName;
	private String categoryName;
	private String categoryNo;
	private String brandNo;
	private String brandName;
	private String spuModel;
	private String seasonYear;
	private String hubSeason;
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
	private BigDecimal supplyPrice;
	private String supplyPriceCurrency;
	private BigDecimal marketPrice;
	private String marketPriceCurrencyOrg;
	private String measurement;
	private String spuDesc;
	private String supplierId;
	public String[] getHubProductTemplate() {
		String [] temp = {"supplierNo","supplierName","categoryName","categoryNo","brandNo","brandName","spuModel","seasonYear","hubSeason","hubGender","supplierSkuNo","skuName","supplierBarcode",
				"hubColor","specificationType","sizeType","hubSkuSize","hubMaterial","hubOrigin","supplyPrice","supplyPriceCurrency","marketPrice","marketPriceCurrencyOrg","measurement","spuDesc"};
		return temp;
	}

}