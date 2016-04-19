package com.shangpin.iog.magway.dto;

import java.util.List;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class GoodsList {

	List<DataG> Data;
	List<Errors> Errors;
	String Status;
	String Message;
}
