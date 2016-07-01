package com.shangpin.iog.lungolivigno.dto;

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
