package com.shangpin.ep.order.module.orderapiservice.impl.dto.tony;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReturnObject {

	private String status;
	private List<Data> data;
}
