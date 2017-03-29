package com.shangpin.ephub.client.data.mysql.sku.dto;

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
public class HubSupplierPriceChangeRecordWithRowBoundsDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2529430660239869178L;

	private HubSupplierPriceChangeRecordCriteriaDto criteria;
	
	private RowBoundsDto rowBounds;
}
