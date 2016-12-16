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
public class HubSpuWithCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -260326594133473196L;

	private HubSpuDto hubSpu;
	
	private HubSpuCriteriaDto criteria;
}