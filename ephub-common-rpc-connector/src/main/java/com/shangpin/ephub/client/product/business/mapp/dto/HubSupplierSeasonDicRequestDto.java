package com.shangpin.ephub.client.product.business.mapp.dto;

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
public class HubSupplierSeasonDicRequestDto {
	
	private Long seasonDicId;
	private String supplierNo;
	private String supplierId;
	private int pageNo;
	private int pageSize;
	private String supplierSeason;
	private String hubMarketTime;
	private String hubSeason;
	private Byte type;
	private Byte filterFlag;//1、当季  0：非当季
	private String updateUser;
	private byte refreshDicType;

}
