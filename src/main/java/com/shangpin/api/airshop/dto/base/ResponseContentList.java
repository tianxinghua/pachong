package com.shangpin.api.airshop.dto.base;

import com.shangpin.api.airshop.util.enums.ApiErrCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 返回前端的消息体内容<br/>
 *
 * Date:     2016年1月11日 <br/>
 * @author   陈小峰
 * @since    JDK 7
 */
@Setter
@Getter
public class ResponseContentList<T> implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 状态码1有问题，0无问题
	 */
	private String code;
	/**
	 * 错误正确提示
	 */
	private String msg;
	private int total;
	/**
	 * 消息体
	 */
	private List<T> content;

	public static <T> ResponseContentList<T> errorParams(){
		return errorResp("1","Request with error params !");
	}

	/**
	 * 针对api返回错误码的包装类
	 * @param apiResCode api 返回错误码
	 * @param <T> 类型
     * @return ResponseContentList
     */
	public static <T> ResponseContentList<T> errorRespWithApi(String apiResCode){
		return errorResp("1", ApiErrCode.getErrorMsg(apiResCode));
	}

	public static <T> ResponseContentList<T> errorResp(String msg){
		return errorResp("1",msg);
	}

	/**
	 * 响应错误消息
	 * @param code 错误码
	 * @param msg 错误消息提示
	 * @return ResponseContentList
	 */
	public static <T> ResponseContentList<T> errorResp(String code, String msg){
		ResponseContentList<T> resp = new ResponseContentList<>();
		resp.setCode(code);
		resp.setMsg(msg);
		return resp;
	}

	/**
	 * 成功响应
	 * @param t
	 * @return ResponseContentList
	 */
	public static <T> ResponseContentList<T> successResp(List<T> t){
		ResponseContentList<T> resp = new ResponseContentList<>();
		resp.setCode("0");
		resp.setMsg("Success");
		resp.setContent(t);
		return resp;
	}
}
