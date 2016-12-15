package com.shangpin.ephub.client.data.mysql.picture.dto;

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
public class HubSpuPendingPicWithCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8098281033321763241L;
	
	private HubSpuPendingPicDto hubSpuPendingPic;
	
	private HubSpuPendingPicCriteriaDto criteria;

}
