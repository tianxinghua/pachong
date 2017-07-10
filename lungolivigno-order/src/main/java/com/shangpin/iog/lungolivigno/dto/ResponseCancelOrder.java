package com.shangpin.iog.lungolivigno.dto;

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
