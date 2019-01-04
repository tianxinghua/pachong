package com.shangpin.iog.channeladvisor.purchase.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {

	String BuyerEmailAddress;
	Integer ProfileID;
	String CreatedDateUtc;
	Double TotalPrice;
	Integer SiteID;
	List<Item> Items;
}
