package com.shangpin.ephub.client.data.mysql.studio.spu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubSlotSpuWithCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4015078084915268295L;

	private HubSlotSpuDto hubSlotSpu;
	
	private HubSlotSpuCriteriaDto criteria;
}
