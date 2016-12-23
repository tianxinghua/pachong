package com.shangpin.ephub.data.mysql.mapping.material.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.mapping.material.po.HubMaterialMapping;
import com.shangpin.ephub.data.mysql.mapping.material.po.HubMaterialMappingCriteria;

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
public class HubMaterialMappingWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4015909962438658783L;

	private HubMaterialMapping hubMaterialMapping;
	
	private HubMaterialMappingCriteria criteria;
}
