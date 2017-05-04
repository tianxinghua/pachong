package com.shangpin.ephub.product.business.ui.mapp.season.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubSupplierSeasonDicResponseDto {
	
	private Long seasonDicId;
	private String supplierId;
	private String supplierNo;
	private String supplierSeason;
	private String hubMarketTime;
	private String hubSeason;
	 private Byte filterFlag;//1、当季  0：非当季
	 private String updateTime;
	 private String createTime;
	 private String updateUser;

}
