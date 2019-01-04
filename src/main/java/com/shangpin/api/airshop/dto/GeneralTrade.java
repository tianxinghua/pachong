package com.shangpin.api.airshop.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GeneralTrade {
	private String category ="General Trade";
	private String orderQty;
	private String totalPrice;
	private String totalWeight;
	private String lastUpdateTime;
}
