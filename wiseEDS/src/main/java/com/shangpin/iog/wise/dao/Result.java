package com.shangpin.iog.wise.dao;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Result {
	
	private String reqCode;
	private String count;
	private List<Item> items;
	
}
