package com.shangpin.ep.order.module.orderapiservice.impl.dto.lungolivigno;


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
