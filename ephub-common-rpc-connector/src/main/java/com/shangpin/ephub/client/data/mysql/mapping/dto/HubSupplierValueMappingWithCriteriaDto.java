package com.shangpin.ephub.client.data.mysql.mapping.dto;

import java.io.Serializable;

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
public class HubSupplierValueMappingWithCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 693081865428093447L;

	private HubSupplierValueMappingDto hubSupplierValueMapping;
	
	private HubSupplierValueMappingCriteriaDto criteria;
}
