package com.shangpin.api.airshop.dto.base;

import com.shangpin.api.airshop.util.enums.ApiErrCode;
import lombok.Getter;
import lombok.Setter;

/**返回前端所需的数据格式
 * @author qinyingchun
 *
 * @param <T>
 */
@Getter
@Setter
public class ResponseContentOne<T> extends ResponseContentBase{

	private static final long serialVersionUID = 1L;

	private T content;

	/**
	 * 请求成功返回的结构数据
	 * @param t t
	 * @return ResponseContentOne
	 */
	public static <T> ResponseContentOne<T> successResp(T t){
		ResponseContentOne<T> resp = new ResponseContentOne<T>();
		resp.setCode("0");
		resp.setMsg("success");
		resp.setContent(t);
		return resp;
	}

	/**
	 * 针对api返回错误码的包装类
	 * @param apiResCode api 返回错误码
	 * @param <T> 类型
	 * @return ResponseContentList
	 */
	public static <T> ResponseContentOne<T> errorRespWithApi(String apiResCode){
		return errorResp("1", ApiErrCode.getErrorMsg(apiResCode));
	}

	/**
	 * 请求失败返回的数据
	 * @param msg msg
	 * @return ResponseContentOne
	 */
	public static <T> ResponseContentOne<T> errorResp(String msg){
		return errorResp("1", msg);
	}

	/**
	 * 请求参数错误数据结构
	 * @return ResponseContentOne
	 */
	public static <T> ResponseContentOne<T> errorParam(){
		return errorResp("1", "Request with error params");
	}

	/**
	 * 请求失败返回的数据
	 * @param msg msg
	 * @return ResponseContentOne
	 */
	public static <T> ResponseContentOne<T> errorResp(String code, String msg){
		ResponseContentOne<T> resp = new ResponseContentOne<>();
		resp.setCode(code);
		resp.setMsg(msg);
		return resp;
	}


}
