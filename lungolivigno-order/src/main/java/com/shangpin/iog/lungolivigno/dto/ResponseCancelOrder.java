package com.shangpin.iog.lungolivigno.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseCancelOrder {

	boolean Result;
	String ErrMessage;
	boolean Status;
	Integer ErrCode;
}
