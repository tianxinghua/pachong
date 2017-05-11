package com.shangpin.ephub.product.business.ui.mapp.brand.dto;

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
public class HubSupplierBrandDicResponseDto {
	
	private Long supplierBrandDicId;
	private Long brandDicId;
	private String supplierId;
	private String supplierNo;
	private String supplierName;
	private String supplierBrand;
	private String hubBrandNo;
	 private String updateTime;
	 private String createTime;
	 private String updateUser;

}
