package com.shangpin.ephub.client.data.mysql.sku.dto;

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
public class HubSkuCriteriaWithRowBoundsDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6065866159180047611L;

	private HubSkuCriteriaDto criteria;
	
	private RowBoundsDto rowBounds;
}