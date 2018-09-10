package com.shangpin.spider.entity.base;

import java.io.Serializable;

/** 
 * @author  njt 
 * @date 创建时间：2017年12月15日 下午3:01:52 
 * @version 1.0 
 * @parameter  
 */

public class StatusEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 状态码
	 */
	private String status;
	/**
	 * 状态描述
	 */
	private String msg;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	

}
