package com.shangpin.ephub.product.business.rest.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SizeDto {

	/**
	 * 供应商门户id
	 */
	private String supplierId;
	/**
	 * 供应商sku编号
	 */
	private String supplierSkuNo;
	/**
	 *  supplierSpuId
	 */
	private Long supplierSpuId;
}
