package com.shangpin.ephub.product.business.ui.pending.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PendingUpdatedVo {

	/**
	 * 
	 */
	private Long spuPendingId;
	/**
	 * spu校验不通过的原因
	 */
	private String spuResult;
	/**
	 * sku校验不通过的原因
	 */
	private List<PendingSkuUpdatedVo> skus;
}
