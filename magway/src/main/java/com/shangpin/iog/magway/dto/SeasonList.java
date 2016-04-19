package com.shangpin.iog.magway.dto;

import java.util.List;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class SeasonList {

	List<DataS> Data;
	List<ErrorsS> Errors;
	String Status;
	String Message;
}
