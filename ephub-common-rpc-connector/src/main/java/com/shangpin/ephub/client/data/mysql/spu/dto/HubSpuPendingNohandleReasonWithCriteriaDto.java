package com.shangpin.ephub.client.data.mysql.spu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubSpuPendingNohandleReasonWithCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -86223563656902875L;

	private HubSpuPendingNohandleReasonDto hubSpuPendingNohandleReason;
	
	private HubSpuPendingNohandleReasonCriteriaDto criteria;
}
