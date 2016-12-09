package com.shangpin.ep.order.module.orderapiservice.impl.dto.inviqa;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FailResult {
	//{"messages":{"error":[{"code":400,"message":"Some order items are out of stock."}]}}

	private Messages messages;
	
}
