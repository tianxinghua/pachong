package com.shangpin.iog.lungolivigno.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestProductsDTO {

	String FromDate;
	String WithStock;
	String PriceList;
	Pagination Pagination; 
}
