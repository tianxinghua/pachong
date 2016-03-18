package com.shangpin.iog.dto;

import lombok.Getter;

import lombok.Setter;

import java.util.*;

@Setter
@Getter
public class OrderResult {

	Data Data;
	List<Errors> Errors;
	String Status;
	String Message;
}
