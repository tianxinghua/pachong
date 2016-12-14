package com.shangpin.ephub.data.mysql.dictionary.brand.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.dictionary.brand.po.HubBrandDic;
import com.shangpin.ephub.data.mysql.dictionary.brand.po.HubBrandDicCriteria;

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
public class HubBrandDicWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7537884039632524577L;

	private HubBrandDic hubBrandDic;
	
	private HubBrandDicCriteria hubBrandDicCriteria;
}
