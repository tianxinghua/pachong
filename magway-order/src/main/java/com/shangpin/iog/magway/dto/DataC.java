package com.shangpin.iog.magway.dto;

import java.util.*;
import lombok.Setter;

import lombok.Getter;

@Getter
@Setter
public class DataC {

	String ConfirmedQty;
	String ConfirmedMoneyEuro;
	String ConfirmedMoneyCN;
	String ConfirmedType;
	List<OrderDetail> OrderDetail;
}
