package com.shangpin.ephub.client.data.mysql.categroy.dto;

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
public class HubSupplierCategroyDicWithCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6329136181642689815L;
	
	private HubSupplierCategroyDicDto hubSupplierCategroyDic;
	
	private HubSupplierCategroyDicCriteriaDto criteria;

}
