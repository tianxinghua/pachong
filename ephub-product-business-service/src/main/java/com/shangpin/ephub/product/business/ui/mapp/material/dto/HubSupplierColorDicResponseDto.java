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
public class HubSupplierColorDicResponseDto {
	
	private Long colorDicItemId;
	private Long colorDicId;
	private String supplierColor;
	private String hubColor;
	private String updateTime;
	private String createTime;
	private String updateUser;

}
