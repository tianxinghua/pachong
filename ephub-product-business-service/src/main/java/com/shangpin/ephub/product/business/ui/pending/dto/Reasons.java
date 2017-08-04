package com.shangpin.ephub.product.business.ui.pending.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reasons {

	/**
	 * 勾选的pending spu主键
	 */
	private List<Long> spuPeningIds;
	/**
	 * 错误原因
	 */
	private List<Reason> reasons;
	/**
	 * 创建人
	 */
	private String createUser;
}
