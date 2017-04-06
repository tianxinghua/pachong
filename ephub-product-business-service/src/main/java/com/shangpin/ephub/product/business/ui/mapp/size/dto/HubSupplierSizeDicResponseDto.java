package com.shangpin.ephub.product.business.ui.mapp.size.dto;

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
public class HubSupplierSizeDicResponseDto {
	
	private Long hubSupplierValMappingId;
	private String supplierId;
	private String hubVal;
	private String supplierVal;
	private int sortVal;
}
