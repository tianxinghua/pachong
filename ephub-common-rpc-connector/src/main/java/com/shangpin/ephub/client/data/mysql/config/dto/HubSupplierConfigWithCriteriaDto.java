package com.shangpin.ephub.client.data.mysql.config.dto;

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
public class HubSupplierConfigWithCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6756921226835815577L;

	private HubSupplierConfigDto hubSupplierConfig;
	
	private HubSupplierConfigCriteriaDto criteria;
}
