package com.shangpin.api.airshop.dto.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true) 
public class ApiContentOne<T> extends ApiContentBase{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	@JsonIgnoreProperties(ignoreUnknown = true) 
	private T MessageRes;
}
