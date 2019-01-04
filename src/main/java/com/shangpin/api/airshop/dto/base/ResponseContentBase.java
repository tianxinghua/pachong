package com.shangpin.api.airshop.dto.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**返回前端所需的数据格式
 * @author qinyingchun
 */
@Getter
@Setter
public class ResponseContentBase implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 返回码
	 */
	private String code;

	/**
	 * 返回信息
	 */
	private String msg;


}
