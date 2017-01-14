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
public class HubResponse<T> extends HubBaseResponse{

	private static final long serialVersionUID = 1L;

	private T content;
	
	private T errorMsg;

	/**
	 * 请求成功返回的结构数据
	 * @param t t
	 * @return ResponseContentOne
	 */
	public static <T> HubResponse<T> successResp(T t){
		HubResponse<T> resp = new HubResponse<T>();
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
	public static <T> HubResponse<T> errorResp(String msg){
		return errorResp("1", msg);
	}
	/**
	 * 请求失败返回的数据
	 * @param errorMsg
	 * @return
	 */
	public static <T> HubResponse<T> errorResp(T errorMsg){
		HubResponse<T> resp = new HubResponse<>();
		resp.setCode("1");
		resp.setErrorMsg(errorMsg);
		return resp;
	}

	/**
	 * 请求失败返回的数据
	 * @param msg msg
	 * @return ResponseContentOne
	 */
	public static <T> HubResponse<T> errorResp(String code, String msg){
		HubResponse<T> resp = new HubResponse<>();
		resp.setCode(code);
		resp.setMsg(msg);
		return resp;
	}


}
