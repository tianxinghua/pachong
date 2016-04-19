package com.shangpin.iog.magway.dto;

import lombok.Setter;

import lombok.Getter;

import java.util.List;

@Getter
@Setter
public class DataDet {

	String ID;
	String Color;
	String Composition;
	String MadeIn;
	List<Stocks> Stocks;
	List<Pictures> Pictures;
	List<Errors> Errors;
	String Status;
	String Message;
	String CreatedTime;
	String ModifiedTime;
	
}
