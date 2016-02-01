package com.shangpin.iog.bernardelli.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Data {
	private String number_of_products;
	private List<Product> product;
}
