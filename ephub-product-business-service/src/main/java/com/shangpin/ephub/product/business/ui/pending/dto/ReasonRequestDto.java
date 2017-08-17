package com.shangpin.ephub.product.business.ui.pending.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReasonRequestDto {

	/**
	 * 错误类型
	 */
	private Byte errorType;
	/**
	 * 错误原因
	 */
	private Byte errorReason;
	
	private Integer pageIndex;
	private Integer pageSize;
	private String supplierId;
}
