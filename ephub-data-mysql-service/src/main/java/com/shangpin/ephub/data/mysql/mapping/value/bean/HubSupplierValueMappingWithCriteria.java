package com.shangpin.ephub.data.mysql.mapping.value.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.mapping.value.po.HubSupplierValueMapping;
import com.shangpin.ephub.data.mysql.mapping.value.po.HubSupplierValueMappingCriteria;

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
public class HubSupplierValueMappingWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 693081865428093447L;

	private HubSupplierValueMapping hubSupplierValueMapping;
	
	private HubSupplierValueMappingCriteria criteria;
}
