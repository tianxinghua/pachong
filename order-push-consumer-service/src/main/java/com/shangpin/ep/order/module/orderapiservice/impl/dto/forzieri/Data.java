package com.shangpin.ep.order.module.orderapiservice.impl.dto.forzieri;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Data {
	private String order_id;
    private Item[] item;
}
