package com.shangpin.ephub.data.mysql.slot.dicbrand.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import com.shangpin.ephub.data.mysql.slot.dicbrand.po.HubDicStudioBrand;
import com.shangpin.ephub.data.mysql.slot.dicbrand.po.HubDicStudioBrandCriteria;
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
public class HubDicStudioBrandWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4015078084915268295L;

	private HubDicStudioBrand hubDicStudioBrand;
	
	private HubDicStudioBrandCriteria criteria;
}
