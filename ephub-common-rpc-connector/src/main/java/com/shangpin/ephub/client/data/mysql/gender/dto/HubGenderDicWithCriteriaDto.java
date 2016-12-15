package com.shangpin.ephub.client.data.mysql.gender.dto;

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
public class HubGenderDicWithCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -383984515755423137L;
	
	private HubGenderDicDto hubGenderDic;
	
	private HubGenderDicCriteriaDto criteria;

}
