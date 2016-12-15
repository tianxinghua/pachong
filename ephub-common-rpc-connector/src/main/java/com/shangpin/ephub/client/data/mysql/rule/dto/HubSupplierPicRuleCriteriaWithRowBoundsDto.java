package com.shangpin.ephub.client.data.mysql.rule.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.client.common.dto.RowBoundsDto;

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
public class HubSupplierPicRuleCriteriaWithRowBoundsDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1086212056781641590L;
	
	private HubSupplierPicRuleCriteriaDto criteria;
	
	private RowBoundsDto rowBounds;

}
