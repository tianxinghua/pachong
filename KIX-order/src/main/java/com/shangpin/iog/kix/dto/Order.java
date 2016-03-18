package com.shangpin.iog.kix.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class Order {
	private String id;
	private List<OrderDetail> line_items;
	private String financial_status;
	private String inventory_behaviour;
	private Customer customer;
}
