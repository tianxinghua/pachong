package com.shangpin.ephub.data.mysql.mapping.sku.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.mapping.sku.po.HubSkuSupplierMapping;
import com.shangpin.ephub.data.mysql.mapping.sku.po.HubSkuSupplierMappingCriteria;

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
public class HubSkuSupplierMappingWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4322376769142396175L;

	private HubSkuSupplierMapping hubSkuSupplierMapping;
	
	private HubSkuSupplierMappingCriteria criteria;
}
