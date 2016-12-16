package com.shangpin.ephub.client.data.mysql.brand.dto;

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
public class HubBrandDicWithCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7537884039632524577L;

	private HubBrandDicDto hubBrandDic;
	
	private HubBrandDicCriteriaDto hubBrandDicCriteria;
}