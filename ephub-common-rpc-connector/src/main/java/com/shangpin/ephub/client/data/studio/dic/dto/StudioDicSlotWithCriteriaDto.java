package com.shangpin.ephub.client.data.studio.dic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudioDicSlotWithCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5103951128810875746L;

	private StudioDicSlotDto studioDicSlot;
	
	private StudioDicSlotCriteriaDto criteria;
}
