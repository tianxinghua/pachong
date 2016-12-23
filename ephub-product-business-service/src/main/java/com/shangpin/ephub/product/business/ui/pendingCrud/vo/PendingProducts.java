package com.shangpin.ephub.product.business.ui.pendingCrud.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PendingProducts {

	private List<PendingProductDto> produts;
	private int total;
}
