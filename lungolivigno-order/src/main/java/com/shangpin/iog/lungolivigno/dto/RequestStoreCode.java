package com.shangpin.iog.lungolivigno.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestStoreCode {

	List<String> Sku;
	boolean WithDetail;
	Pagination Pagination;
}
