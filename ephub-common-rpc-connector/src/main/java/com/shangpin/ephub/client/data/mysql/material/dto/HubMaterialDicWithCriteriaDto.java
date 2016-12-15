package com.shangpin.ephub.client.data.mysql.material.dto;

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
public class HubMaterialDicWithCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 304689801871139496L;

	private HubMaterialDicDto hubMaterialDic;
	
	private HubMaterialDicCriteriaDto criteria;
}
