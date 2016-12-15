package com.shangpin.ephub.data.mysql.dictionary.categroy.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.data.mysql.dictionary.categroy.po.HubSupplierCategroyDic;
import com.shangpin.ephub.data.mysql.dictionary.categroy.po.HubSupplierCategroyDicCriteria;

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
public class HubSupplierCategroyDicWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6329136181642689815L;
	
	private HubSupplierCategroyDic hubSupplierCategroyDic;
	
	private HubSupplierCategroyDicCriteria criteria;

}
