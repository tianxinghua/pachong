package com.shangpin.ephub.client.data.mysql.season.dto;

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
public class HubSeasonDicWithCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7981932787818888641L;

	private HubSeasonDicDto hubSeasonDic;
	
	private HubSeasonDicCriteriaDto criteria;
}
