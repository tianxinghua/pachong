package com.shangpin.ephub.product.business.ui.studio.incomingslots.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmQuery {
	
	private String studioNo;

	/**
	 * 到货签收人
	 */
	private String arriveUser;
	/**
	 * 已确认到货的批次主键
	 */
	private List<Long> ids;
}
