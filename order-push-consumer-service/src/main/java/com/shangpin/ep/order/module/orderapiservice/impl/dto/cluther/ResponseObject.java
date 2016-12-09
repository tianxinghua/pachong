package com.shangpin.ep.order.module.orderapiservice.impl.dto.cluther;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseObject {
	private String id;
	private String message;
	private String purchaseNo;
    private String orderNo;
    private String status;
    
}
