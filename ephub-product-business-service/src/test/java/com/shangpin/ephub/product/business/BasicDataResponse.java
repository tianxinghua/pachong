package com.shangpin.ephub.product.business;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicDataResponse <T> {

	@JsonProperty("ResDatas")
	private T resDatas;
	@JsonProperty("IsSuccess")
	private boolean isSuccess;
	@JsonProperty("ResMsg")
	private String resMsg;
}
