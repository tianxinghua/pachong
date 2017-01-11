package com.shangpin.ephub.client.product.business.hubpending.spu.result;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PendingProducts {

	private List<PendingProductDto> produts;
	private int total;
	private String createUser;
}
