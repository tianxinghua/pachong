package com.shangpin.iog.lungolivigno.dto;

import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestStokDTO {

	Set<String> Sku;
	boolean WithDetail;
	Pagination Pagination;
}
