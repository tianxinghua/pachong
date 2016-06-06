package com.shangpin.iog.magway.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderItems {

	String OrderCode;
	Integer GID;
	Integer Count;
	String Size;
	String PriceCN;
	String PriceEuro;
	String TotalMoneyCN;
	String TotalMoneyEuro;
}
