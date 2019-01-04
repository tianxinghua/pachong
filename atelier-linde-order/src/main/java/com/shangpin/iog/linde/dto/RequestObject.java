package com.shangpin.iog.linde.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestObject {

	private String order_number;
	private String date;
	private String items_count;
	private Item [] items;

}
