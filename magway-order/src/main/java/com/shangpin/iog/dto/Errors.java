package com.shangpin.iog.dto;

import java.util.List;
import lombok.Setter;

import lombok.Getter;

@Getter
@Setter
public class Errors {

	String Field;
	List ErrorMessages;
}
