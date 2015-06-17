/**
 * 
 */
package com.shangpin.iog.coltorti.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.coltorti.dto.ColtortiError;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月5日
 */
public class ColtortiUtil {
	public static String supplier="S0000238";
	static String tokenExpire="token has expired";
	static String noResult="no results found";
	/**
	 * 判断返回的信息是否是token过期信息 ，并重新初始化token
	 * @param responseBody
	 * @return
	 * @throws ServiceException
	 */
	public static ColtortiError check(String responseBody) throws ServiceException{
		if(StringUtils.isEmpty(responseBody))
			throw new ServiceMessageException(noResult);
		JsonElement je=new JsonParser().parse(responseBody);
		JsonElement msg=je.getAsJsonObject().get("message");
		if(msg!=null){
			throw new ServiceMessageException(msg.getAsString());
		}
		return null;
	}
	
	/**
	 * get请求方式的url带上参数
	 * @param url 请求url
	 * @param param get参数
	 * @return
	 * @throws ServiceException 
	 */
	public static String paramGetUrl(String url, Map<String, String> param) throws ServiceException {
		Set<String> keys =param.keySet();
		StringBuffer sb = new StringBuffer(url);sb.append("?");
		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			sb.append(key).append("=").append(param.get(key)).append("&");
		}
		return sb.toString();
	}
	/**
	 * 获取公共参数,包括token
	 * @param page 可选页码
	 * @param size 可选页大小
	 * @return
	 * @throws ServiceException 获取token失败的异常
	 */
	public static Map<String, String> getCommonParam(int page, int size) throws ServiceException {
		Map<String,String> param = new HashMap<String, String>();
		if(page>0) param.put("page", page+"");if(size>0) param.put("limit", size+"");
		param.put("access_token", ColtortiTokenService.getToken());
		return param;
	}

	/**
	 * 是否没有结果的异常
	 * @param e
	 * @return
	 */
	public static boolean isNoResultError(ServiceException e) {
		String msg=e.getMessage();
		if(noResult.equals(msg))
			return true;
		return false;
	}

	/**
	 * 是否token过期异常提示
	 * @param e
	 * @return
	 */
	public static boolean isTokenExpire(ServiceException e) {
		String msg=e.getMessage();
		if(tokenExpire.equals(msg))
			return true;
		return false;
	}
}
