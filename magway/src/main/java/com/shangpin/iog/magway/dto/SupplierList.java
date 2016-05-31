package com.shangpin.iog.magway.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierList {

	List<DateSupplier> Data;
	List<Errors> Errors;
	String Status;
	String Message;
}
