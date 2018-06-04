package com.shangpin.ephub.client.product.business.mapp.dto;

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
	
	private Long colorDicItemId;
	private Long colorDicId;
	private int pageNo;
	private int pageSize;
	private String hubColor;
	private String supplierColor;
	private Byte type;
	private String updateUser;
	private String createUser;
	private Byte refreshDicType;
}
