package com.shangpin.ep.order.module.orderapiservice.impl.dto.coltorti;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderJson {
	private String order_id;
	private Customer customer;
	private List<Product> products;
}
