package com.shangpin.ep.order.module.orderapiservice.impl.dto.lungolivigno;


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
