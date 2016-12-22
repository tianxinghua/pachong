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
	
	private String categoryName;
	private String categoryNo;
	private String brandNo;
	private String brandName;
	private String productCode;
	private String gender;
	private String color;
	private String sizeType;
	private String size;
	private String material;
	private String madeIn;
	private String marketPrice;
	private String marketCurrency;

}
