package com.shangpin.iog.magway.dto;

import java.util.*;
import lombok.Setter;

import lombok.Getter;

@Getter
@Setter
public class ConfirmResult {

	DataC Data;
	List<Errors> Errors;
	String Status;
	String Message;
}
