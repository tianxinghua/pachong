package com.shangpin.asynchronous.task.consumer.productimport.hub.dto;

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
public class HubProductImportDTO {
	
	private String supplierNo;
	private String supplierName;
	private String categoryName;
	private String categoryNo;
	private String brandNo;
	private String brandName;
	private String spuModel;
	private String gender;
	private String hubColor;
	private String season;
	private String marketTime;
	private String sizeType;
	private String skuSize;
	private String material;
	private String origin;
	private String marketPrice;
	private String marketCurrency;

	 /**
	  * 和上面的属性保持一致
	  * @return
	  */
	 public static String [] getHubProductTemplate(){
		 String[] headers = { "supplierNo", "supplierName", "categoryName", "categoryNo","brandNo","brandName","spuModel","gender",
					"hubColor","season","marketTime","sizeType","skuSize","material","origin","marketPrice","marketCurrency"};
		 return headers;
	 }
}
