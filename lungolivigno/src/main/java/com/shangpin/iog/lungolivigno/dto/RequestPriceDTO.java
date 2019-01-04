package com.shangpin.iog.lungolivigno.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPriceDTO {

	List<String> Sku;
	String PriceList;
	Pagination Pagination;
}
