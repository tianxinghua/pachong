package com.shangpin.api.airshop.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GeneralTradeAndCrossBorderDetail {
	
	private String category;
	private String orderQty;
	private String totalPrice;
	private String totalWeight;
	private String lastUpdateTime;
	private String supplierOrderOrPurchNo;
	private String sopPurchaseOrderNo;

}
