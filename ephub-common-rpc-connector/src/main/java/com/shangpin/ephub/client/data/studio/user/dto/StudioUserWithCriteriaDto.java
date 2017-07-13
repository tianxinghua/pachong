package com.shangpin.ephub.client.data.studio.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioCriteriaDto;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioDto;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudioUserWithCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5103951128810875746L;

	private StudioUserDto  studioUser ;

	private StudioUserCriteriaDto criteria;
}
