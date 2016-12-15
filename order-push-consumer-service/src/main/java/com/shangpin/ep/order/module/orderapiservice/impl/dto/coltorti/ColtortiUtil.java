/**
 * 
 */
package com.shangpin.ep.order.module.orderapiservice.impl.dto.coltorti;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shangpin.ep.order.exception.ServiceException;
import com.shangpin.ep.order.exception.ServiceMessageException;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月5日
 */
public class ColtortiUtil {

//	public static String supplier;
//	public static String productSupplierId;//拉取产品和线上的SUPPLIER_ID 分开
//
//	static {
//		if(null==bdl)
//			bdl=ResourceBundle.getBundle("coltorti");
//		supplier = bdl.getString("supplierId");
//		productSupplierId = bdl.getString("productSupplierId");
//	}

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
			throw new ServiceMessageException("http 返回空"+noResult);

		JsonElement je=new JsonParser().parse(responseBody);
		JsonObject jo=je.getAsJsonObject();
		
		JsonElement msg=jo.get("message");
		if(msg!=null){
			throw new ServiceMessageException(msg.getAsString());
		}
		/*msg=jo.get("access_token");
		if(tokenExpire.equals(msg))
			throw new ServiceMessageException(msg.getAsString());	*/

		if(HttpUtil45.errorResult.equals(responseBody)){
			throw new ServiceMessageException("http访问错误！");
		}

		if(HttpUtil45.errorResult.equals(msg)){
			throw new ServiceMessageException("http访问错误！");
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
