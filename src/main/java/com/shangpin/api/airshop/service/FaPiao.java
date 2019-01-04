package com.shangpin.api.airshop.service;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FaPiao {

	private int page;
	private List<List> list;
	private List<List> detailList;
	private Invoice invoice;
	
	private String sendTime;
}
