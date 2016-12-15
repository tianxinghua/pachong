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
public class HubColorDicItemWithCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 912241422235144650L;
	
	private HubColorDicItemDto hubColorDicItem;
	
	private HubColorDicItemCriteriaDto criteria;

}
