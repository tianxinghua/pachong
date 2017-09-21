package com.shangpin.ephub.client.data.mysql.sp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.client.common.dto.RowBoundsDto;
import lombok.*;


import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpShippingPolicyDetailCriteriaWithRowBoundsDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7255745601518578512L;

	private SpShippingPolicyDetailCriteriaDto criteria;
	
	private RowBoundsDto rowBounds;
}
