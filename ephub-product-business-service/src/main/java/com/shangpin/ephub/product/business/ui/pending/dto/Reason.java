package com.shangpin.ephub.product.business.ui.pending.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reason {

	/**
	 * 错误类型
	 */
	private String errorType;
	/**
	 * 错误原因
	 */
	private String errorReason;
}
