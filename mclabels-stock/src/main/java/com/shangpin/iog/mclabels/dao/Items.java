package com.shangpin.iog.mclabels.dao;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Items {

	private int total;
	private int pages;
	private int page;
	private List<Item> results;
}