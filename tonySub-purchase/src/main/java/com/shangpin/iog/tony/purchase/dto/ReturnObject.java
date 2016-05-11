package com.shangpin.iog.tony.purchase.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReturnObject {

	private String status;
	private List<Data> data;
}
