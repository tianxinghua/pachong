package com.shangpin.ephub.product.business.ui.mapp.gender.dto;

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
public class HubSupplierGenderDicRequestDto {
	
	private Long genderDicId;
	private int pageNo;
	private int pageSize;
	private String hubGender;
	private String supplierGender;
	private Byte type;
	private String updateUser;
	private String createUser;
}
