package com.shangpin.iog.magway.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsIdList {

	List<Integer> Data;
	List<Errors> Errors;
	String Status;
	String Message;
}
