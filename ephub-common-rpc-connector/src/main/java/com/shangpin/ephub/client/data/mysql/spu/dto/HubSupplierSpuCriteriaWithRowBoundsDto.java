package com.shangpin.ephub.client.data.mysql.spu.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.client.common.dto.RowBoundsDto;

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
public class HubSupplierSpuCriteriaWithRowBoundsDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7953439026087059715L;

	private HubSupplierSpuCriteriaDto criteria;
	
	private RowBoundsDto rowBounds;
}
