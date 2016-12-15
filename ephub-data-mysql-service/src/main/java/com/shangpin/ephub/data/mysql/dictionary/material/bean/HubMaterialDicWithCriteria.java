package com.shangpin.ephub.data.mysql.dictionary.material.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.dictionary.material.po.HubMaterialDic;
import com.shangpin.ephub.data.mysql.dictionary.material.po.HubMaterialDicCriteria;

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
public class HubMaterialDicWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 304689801871139496L;

	private HubMaterialDic hubMaterialDic;
	
	private HubMaterialDicCriteria criteria;
}
