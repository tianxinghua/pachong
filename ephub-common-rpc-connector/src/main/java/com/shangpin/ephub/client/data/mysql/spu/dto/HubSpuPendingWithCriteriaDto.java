package com.shangpin.ephub.client.data.mysql.spu.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
public class HubSpuPendingWithCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -86223563656902875L;

	private HubSpuPendingDto hubSpuPending;
	
	private HubSpuPendingCriteriaDto criteria;
}
