package com.shangpin.iog.channeladvisor.purchase.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {

	String sku;
	String unitPrice;
	String shippingPrice;
	Integer quantity;
	List<Promotion> promotions;
}
