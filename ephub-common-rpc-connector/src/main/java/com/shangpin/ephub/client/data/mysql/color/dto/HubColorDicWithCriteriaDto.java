package com.shangpin.ephub.client.data.mysql.color.dto;

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
public class HubColorDicWithCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4759716339997637156L;

	private HubColorDicDto hubColorDic;
	
	private HubColorDicCriteriaDto hubColorDicCriteria;
}
