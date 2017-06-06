package com.shangpin.ephub.client.data.studio.slot.defective.dto;

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
public class StudioSlotDefectiveSpuCriteriaWithRowBoundsDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5973912267137982230L;

	private StudioSlotDefectiveSpuCriteriaDto criteria;
	
	private RowBoundsDto rowBounds;
}
