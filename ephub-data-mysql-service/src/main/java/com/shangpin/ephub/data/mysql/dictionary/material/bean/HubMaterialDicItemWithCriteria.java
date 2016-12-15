package com.shangpin.ephub.data.mysql.dictionary.material.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.dictionary.material.po.HubMaterialDicItem;
import com.shangpin.ephub.data.mysql.dictionary.material.po.HubMaterialDicItemCriteria;

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
public class HubMaterialDicItemWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8843044120654420415L;

	private HubMaterialDicItem hubMaterialDicItem;
	
	private HubMaterialDicItemCriteria criteria;
}
