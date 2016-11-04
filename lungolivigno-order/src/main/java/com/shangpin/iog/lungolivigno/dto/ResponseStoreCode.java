package com.shangpin.iog.lungolivigno.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseStoreCode {

	List<Result> Result;
	String ErrMessage;
	boolean Status;
	int ErrCode;
}
