package com.shangpin.iog.lungolivigno.dto;

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
