package com.shangpin.ep.order.module.orderapiservice.impl.dto.forzieri;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Item {
	private String sku;
	private String quantity;
	private String merchant_sku;
}
