package com.shangpin.ep.order.module.orderapiservice.impl.dto.lungolivigno;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseSaveOrderDTO {

	String Result;
	String ErrMessage;
	boolean Status;
	Integer ErrCode;
}
