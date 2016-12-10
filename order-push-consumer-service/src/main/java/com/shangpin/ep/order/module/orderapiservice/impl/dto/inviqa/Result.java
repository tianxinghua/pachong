package com.shangpin.ep.order.module.orderapiservice.impl.dto.inviqa;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Result {
	
	private String id;
	private String message;
	private String status;
	private String order_no;
	private String purchase_no;
}
