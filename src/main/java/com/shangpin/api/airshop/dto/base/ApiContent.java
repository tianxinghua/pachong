package com.shangpin.api.airshop.dto.base;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiContent<T> extends ApiContentBase{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private List<T> messageRes;
}
