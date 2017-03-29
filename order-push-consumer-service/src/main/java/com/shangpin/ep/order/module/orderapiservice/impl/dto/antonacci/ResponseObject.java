package com.shangpin.ep.order.module.orderapiservice.impl.dto.antonacci;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseObject {
	private String id;
	private String message;
	private String purchase_no;
    private String order_no;
    private String status;
    private String error;
    
}
