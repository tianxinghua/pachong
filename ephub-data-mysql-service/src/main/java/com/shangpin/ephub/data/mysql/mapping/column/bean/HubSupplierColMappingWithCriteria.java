package com.shangpin.ephub.data.mysql.mapping.column.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.mapping.column.po.HubSupplierColMapping;
import com.shangpin.ephub.data.mysql.mapping.column.po.HubSupplierColMappingCriteria;

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
public class HubSupplierColMappingWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6590643894100769713L;

	private HubSupplierColMapping hubSupplierColMapping;
	
	private HubSupplierColMappingCriteria criteria;
}
