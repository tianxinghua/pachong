package com.shangpin.ephub.client.product.business.hubpending.spu.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NohandleReason {

	/**
	 * 门户
	 */
	private String supplierId;
	/**
	 * 供应商spu no.
	 */
	private String supplierSpuNo;
	/**
	 * 无法处理原因
	 */
	private List<String> reasons;
	
}
