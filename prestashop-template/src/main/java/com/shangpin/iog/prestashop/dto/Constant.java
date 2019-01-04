package com.shangpin.iog.prestashop.dto;

public enum Constant {

	NAMESPACE("http://www.w3.org/1999/xlink");

	private String description;

	Constant(String description) {
		this.description = description;
	}

	public final String getDescription() {
		return description;
	}
}
