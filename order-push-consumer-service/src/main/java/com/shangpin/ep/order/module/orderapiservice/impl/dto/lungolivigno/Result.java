package com.shangpin.ep.order.module.orderapiservice.impl.dto.lungolivigno;


import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {

	String Sku;
	List<Sizes> Sizes; 
	String StoreCode;
}
