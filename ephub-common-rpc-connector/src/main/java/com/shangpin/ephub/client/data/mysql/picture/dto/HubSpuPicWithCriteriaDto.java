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
public class HubSpuPicWithCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3998232620688622090L;

	private HubSpuPicDto hubSpuPic;
	
	private HubSpuPicCriteriaDto criteria;
}
