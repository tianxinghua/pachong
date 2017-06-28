package com.shangpin.ep.order.module.orderapiservice.impl.dto.forzieri;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponse {
	private String status;
	private Data data;
	private String errorCode;
}
