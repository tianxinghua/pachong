package com.shangpin.api.airshop.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CrossBorder {

	private String category = "Cross Border";
	private String orderQty;
	private String totalPrice;
	private String totalWeight;
	private String lastUpdateTime;
}
