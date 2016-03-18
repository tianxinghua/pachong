package com.shangpin.iog.dto;

import lombok.Setter;

import lombok.Getter;

import java.util.List;

@Getter
@Setter
public class CreateOrder {

	String Name;
	String Mobile;
	String Address;
	String Country;
	String ZipCode;
	String IDCardNumber;
	String IDCardPathOne;
	String IDCardPathTwo;
	List<Goods> Goods;
	
}
