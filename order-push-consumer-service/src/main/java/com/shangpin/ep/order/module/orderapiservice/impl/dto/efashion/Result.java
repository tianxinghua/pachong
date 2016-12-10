package com.shangpin.ep.order.module.orderapiservice.impl.dto.efashion;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Result {
	
	private String reqCode;
	private String description;
	private String result;
	@Override
	public String toString() {
		return "Result [reqCode=" + reqCode + ", description=" + description
				+ ", result=" + result + ", getReqCode()=" + getReqCode()
				+ ", getDescription()=" + getDescription() + ", getResult()="
				+ getResult() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}
	
}
