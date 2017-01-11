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
	private String spuModel;
	private String marketTime;
	private String season;
	private String gender;
	private String hubColor;
	private String sizeType;
	private String skuSize;
	private String material;
	private String origin;

//	 public static String [] getHubProductValueTemplate(){
//		 String[] headers = { "categoryName", "categoryNo","brandNo","brandName","spuModel","marketTime","season","gender",
//					"hubColor","sizeType","skuSize","material","origin"};
//		 return headers;
//	 }
}
