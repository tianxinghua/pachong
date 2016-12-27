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
	private String supplierId;
	private String supplierName;
	private String spuPendingId;
	private String categoryName;
	private String categoryNo;
	private String brandNo;
	private String brandName;
	private String spuModel;
	private String seasonYear;
	private String hubSeason;
	private String hubGender;
	private String spuName;
	private String hubColor;
	private String hubMaterial;
	private String hubOrigin;
	private String spuDesc;
	public String[] getPendingSpuTemplate() {
		String [] temp = {"supplierId","supplierName","categoryName","categoryNo","brandNo","brandName","spuModel","seasonYear","hubSeason","hubGender",
				"spuName","hubColor","hubMaterial","hubOrigin","spuDesc"};
		return temp;
	}

}
