package com.shangpin.ephub.data.mysql.dictionary.brand.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.dictionary.brand.po.HubSupplierBrandDic;
import com.shangpin.ephub.data.mysql.dictionary.brand.po.HubSupplierBrandDicCriteria;

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
public class HubSupplierBrandDicWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4929893606688978785L;

	private HubSupplierBrandDic hubSupplierBrandDic;
	
	private HubSupplierBrandDicCriteria criteria;
}
