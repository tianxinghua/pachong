package com.shangpin.iog.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CancelResult {

	List<ErrorsC> Errors;
	String Status;
	String Message;
}
