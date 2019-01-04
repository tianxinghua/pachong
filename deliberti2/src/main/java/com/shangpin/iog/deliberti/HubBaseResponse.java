package com.shangpin.iog.deliberti;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 返回前台数据格式
 * @author zhaogenchun
 * @date 2016年12月17日 下午2:57:23
 * @param <T>
 */
@Getter
@Setter
public class HubBaseResponse implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	   private String code;
	    private String msg;
	    private byte[] content;



}
