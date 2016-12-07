package com.shangpin.supplier.product.message.common.e;

/**
 *@Project: scm-message-service
 *@Author: yanxiaobin
 *@Date: 2016年10月25日
 *@Copyright: 2016 www.shangpin.com Inc. All rights reserved.
 */
public enum Code {
	/**
	 * 0表示业务请求成功
	 */
	OK(0, "请求成功"),
	/**
	 * 1表示业务请求失败 
	 */
	NO(1, "请求失败");
	/**
	 * 业务状态码
	 */
	private int code;
	/**
	 * 状态码描述信息
	 */
	private String message;
	/**
	 * @param code 业务状态码
	 * @param message 状态码描述信息
	 */
	private Code(int code, String message) {
		this.code = code;
		this.message = message;
	}
	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}