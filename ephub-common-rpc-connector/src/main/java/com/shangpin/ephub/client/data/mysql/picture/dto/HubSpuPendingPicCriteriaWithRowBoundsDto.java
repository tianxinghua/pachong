package com.shangpin.ephub.client.data.mysql.picture.dto;

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
public class HubSpuPendingPicCriteriaWithRowBoundsDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 128349967638153096L;

	private HubSpuPendingPicCriteriaDto criteria;
	
	private RowBoundsDto rowBounds;
}
