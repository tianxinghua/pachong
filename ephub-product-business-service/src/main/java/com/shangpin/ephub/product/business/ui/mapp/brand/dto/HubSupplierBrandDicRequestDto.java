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
public class HubSupplierBrandDicRequestDto {
	
	private Long supplierBrandDicId;
	private Long brandDicId;
	private String supplierNo;
	private String supplierId;
	private int pageNo;
	private int pageSize;
	private String hubBrandNo;
	private String supplierBrand;
	private Byte type;
	private String updateUser;
	private byte refreshDicType;
	private String startTime;
	private String endTime;
}
