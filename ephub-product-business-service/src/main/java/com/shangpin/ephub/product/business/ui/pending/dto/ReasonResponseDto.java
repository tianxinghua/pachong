package com.shangpin.ephub.product.business.ui.pending.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReasonResponseDto {

	/**
	 * 错误类型
	 */
	private Byte errorType;
	/**
	 * 错误原因
	 */
	private Byte errorReason;
	private String spuName;
	private String brandName;
	private String supplierSpuNo;
	private String spuModel;
	private String errorDesc;
	private String supplierId;
}
