package com.shangpin.iog.bernardelli.stock.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Items {
	private String number_of_items;
	private List<Item> item;
}
