package com.shangpin.iog.magway.dto;

import java.util.*;
import lombok.Setter;

import lombok.Getter;

@Getter
@Setter
public class Data {

	String TradeCode;
	String DealerCode;
	String TotalMoneyCN;
	String TotalMoneyEuro;
	List<OrderItems> OrderItems; 
}
