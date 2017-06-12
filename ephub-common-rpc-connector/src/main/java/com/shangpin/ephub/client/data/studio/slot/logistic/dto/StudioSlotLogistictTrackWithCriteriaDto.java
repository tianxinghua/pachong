package com.shangpin.ephub.client.data.studio.slot.logistic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudioSlotLogistictTrackWithCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5103951128810875746L;

	private StudioSlotLogistictTrackDto  studioSlotLogistictTrackDto;
	
	private StudioSlotLogistictTrackCriteriaDto  criteria;
}
