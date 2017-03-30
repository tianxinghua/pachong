package com.shangpin.ephub.client.data.mysql.sku.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubSupplierPriceChangeRecordWithCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8549460796668466239L;

	private HubSupplierPriceChangeRecordDto hubSupplierPriceChangeRecordDto;
	
	private HubSupplierPriceChangeRecordCriteriaDto criteria;
}
