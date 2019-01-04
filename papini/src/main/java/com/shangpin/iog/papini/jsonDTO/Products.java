package com.shangpin.iog.papini.jsonDTO;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Products {

	private String number;
	private List<Product> product;
}
