package com.shangpin.iog.dto;

import java.util.*;
import lombok.Setter;

import lombok.Getter;

@Getter
@Setter
public class Data {

	String OrderCode;
	String TotalPriceCN;
	String TotalPriceEuro;
	List<OrderGoods> OrderGoods; 
}
