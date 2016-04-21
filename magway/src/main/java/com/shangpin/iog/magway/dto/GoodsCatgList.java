package com.shangpin.iog.magway.dto;

import java.util.List;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class GoodsCatgList {

	List<DataCat> Data;
	List<ErrorsCat> Errors;
	String Status;
	String Message;
}
