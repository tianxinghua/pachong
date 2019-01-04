package com.shangpin.iog.magway.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderResponse {

	String OrderCode;
	Integer GoodsID;
	String Size;
	Integer GoodsCount;
	String TotalMoneyCN;
	Integer OrderStatus;
}
