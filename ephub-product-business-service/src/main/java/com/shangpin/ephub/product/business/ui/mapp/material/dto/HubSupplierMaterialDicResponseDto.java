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
public class HubSupplierMaterialDicResponseDto {
	
	private Long materialMappingId;
	private String supplierMaterial;
	private Byte mappingLevel;
	private String hubMaterial;
	private String updateTime;
	private String createTime;
	private String updateUser;

}
