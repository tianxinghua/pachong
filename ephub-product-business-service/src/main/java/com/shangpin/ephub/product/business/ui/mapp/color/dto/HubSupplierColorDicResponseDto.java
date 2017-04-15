package com.shangpin.ephub.product.business.ui.mapp.color.dto;

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
public class HubSupplierColorDicResponseDto {
	
	private Long supplierCategoryDicId;
	private String supplierId;
	private String supplierNo;
	private String supplierName;
	private String supplierCategory;
	private String supplierGender;
	private String hubCategoryNo;
	 private Byte categoryType;
	 private String updateTime;
	 private String createTime;
	 private String updateUser;

}
