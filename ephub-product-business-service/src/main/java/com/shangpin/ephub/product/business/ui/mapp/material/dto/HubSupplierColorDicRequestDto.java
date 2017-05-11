package com.shangpin.ephub.product.business.ui.mapp.material.dto;

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
	
	private Long material_mapping_id;
	private int pageNo;
	private int pageSize;
	private String supplierMaterial;
	private String hubMaterial;
	private Byte mappingLevel;
	private String updateUser;
	private String createUser;
}
