package com.shangpin.ephub.client.data.mysql.brand.dto;

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
public class HubBrandDicCriteriaWithRowBoundsDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1134035905264295567L;

	private HubBrandDicCriteriaDto criteria;
	
	private RowBoundsDto rowBounds;
}
