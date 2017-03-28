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
public class HubSupplierCategoryDicRequestDto {
	
	private Long id;
	private String supplierId;
	private int pageNo;
	private int pageSize;
	private String hubCategoryNo;
	private String supplierCategoryName;
	private String supplierGender;
	private Byte matchState;
	private String updateUser;

}
