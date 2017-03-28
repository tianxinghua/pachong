package com.shangpin.ephub.product.business.ui.mapp.category.dto;

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
public class HubSupplierCategoryDicResponseDto {
	
	private Long supplierCategoryDicId;
	private String supplierId;
	private String supplierCategory;
	private String supplierGender;
	private String hubCategoryNo;
	 private Byte categoryType;

}
