package com.shangpin.iog.magway.dto;

import java.util.List;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class BrandList {
	
	List<Data> Data;
	List<Errors> Errors;
	String Status;
	String Message;
}
