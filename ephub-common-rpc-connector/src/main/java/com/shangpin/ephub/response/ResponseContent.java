package com.shangpin.ephub.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 返回前台数据格式
 * @author zhaogenchun
 * @date 2016年12月17日 下午2:57:23
 * @param <T>
 */
@Getter
@Setter
public class ResponseContent<T> extends ResponseContentBase{

	private static final long serialVersionUID = 1L;

	private T content;

	/**
	 * 请求成功返回的结构数据
	 * @param t t
	 * @return ResponseContentOne
	 */
	public static <T> ResponseContent<T> successResp(T t){
		ResponseContent<T> resp = new ResponseContent<T>();
		resp.setCode("0");
		resp.setMsg("success");
		resp.setContent(t);
		return resp;
	}

	/**
	 * 请求失败返回的数据
	 * @param msg msg
	 * @return ResponseContentOne
	 */
	public static <T> ResponseContent<T> errorResp(String msg){
		return errorResp("1", msg);
	}

	/**
	 * 请求失败返回的数据
	 * @param msg msg
	 * @return ResponseContentOne
	 */
	public static <T> ResponseContent<T> errorResp(String code, String msg){
		ResponseContent<T> resp = new ResponseContent<>();
		resp.setCode(code);
		resp.setMsg(msg);
		return resp;
	}


}
