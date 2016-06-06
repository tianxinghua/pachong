package com.shangpin.iog.magway.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeResponse {

	String TradeCode;
	String DealerTradeCode;
	Integer GoodsCount;
	String TotalMoneyCN;
	Integer TradeStatus;
	List<OrderResponse> OrderList;
}
