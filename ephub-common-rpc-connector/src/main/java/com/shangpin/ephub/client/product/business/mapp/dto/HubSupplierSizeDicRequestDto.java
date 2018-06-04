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
public class HubSupplierSizeDicRequestDto {
	private Long hubSupplierValMappingId;
	private String supplierNo;
	private String supplierId;
	private int pageNo;
	private int pageSize;
	private String supplierVal;
	private String hubVal;;
	private Byte type;
	private Short sortVal;
	private String updateUser;
	private byte mappingType;
	public byte refreshDicType;

}
