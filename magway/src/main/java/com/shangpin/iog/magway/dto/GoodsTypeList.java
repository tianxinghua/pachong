package com.shangpin.iog.magway.dto;

import java.util.List;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class GoodsTypeList {

	List<DataT> Data;
	List<ErrorsT> Errors;
	String Status;
	String Message;
}
