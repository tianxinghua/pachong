package com.shangpin.api.airshop.dto.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ApiContentBase implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 响应吗
	 */
	private String resCode;

	/**
	 * 是否正确
	 */
	private Boolean isSuccess;



}
