package com.shangpin.ephub.client.data.mysql.spu.dto;

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
public class HubSpuPendingCriteriaWithRowBoundsDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3930645599632142208L;

	private HubSpuPendingCriteriaDto criteria;
	
	private RowBoundsDto rowBounds;
}
