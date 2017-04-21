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
public class HubSupplierColorDicRequestDto {
	
	private Long supplierCategoryDicId;
	private String supplierNo;
	private String supplierId;
	private int pageNo;
	private int pageSize;
	private String hubCategoryNo;
	private String supplierCategory;
	private String supplierGender;
	private Byte categoryType;
	private String updateUser;
	
	

}
