package com.shangpin.ephub.product.business.ui.pending.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PendingSkuUpdatedVo {

	private Long skuPendingId;
	/**
	 * sku校验不通过的原因
	 */
	private String skuResult;
}
