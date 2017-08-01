package com.shangpin.ephub.client.data.mysql.spu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.shangpin.ephub.client.common.dto.RowBoundsDto;
import lombok.*;


import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubSpuPendingNohandleReasonCriteriaWithRowBoundsDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3930645599632141208L;

	private HubSpuPendingNohandleReasonCriteriaDto criteria;
	
	private RowBoundsDto rowBounds;
}
