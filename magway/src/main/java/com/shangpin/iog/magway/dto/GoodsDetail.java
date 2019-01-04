package com.shangpin.iog.magway.dto;

import lombok.Setter;

import lombok.Getter;

import java.util.List;

@Getter
@Setter
public class GoodsDetail {

	DataDet Data;
	List<Errors> Errors;
	String Status;
	String Message;
}
