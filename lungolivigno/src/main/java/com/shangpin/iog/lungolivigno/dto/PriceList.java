package com.shangpin.iog.lungolivigno.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriceList {

	private List<PriceListResult> Result;
	private String ErrMessage;
	private boolean Status; 
	private int ErrCode;
}
