package com.shangpin.ep.order.rest.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class OrderDetailRequestDto {
	
	private String supplierId;
	private String spSkuNo;
	private String masterOrderNo;

}
