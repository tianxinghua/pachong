package com.shangpin.ep.order.module.orderapiservice.impl.dto.lungolivigno;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseCancelOrder {

	Integer Result;
	String ErrMessage;
	boolean Status;
	Integer ErrCode;
}
