package com.shangpin.iog.magway.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ColorList {

	List<DataC> Data;
	List<ErrorsC> Errors;
	String Status;
	String Message;
}
