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
public class HubMaterialDicItemWithCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8843044120654420415L;

	private HubMaterialDicItemDto hubMaterialDicItem;
	
	private HubMaterialDicItemCriteriaDto criteria;
}
