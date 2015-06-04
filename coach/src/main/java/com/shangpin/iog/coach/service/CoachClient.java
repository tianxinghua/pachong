/**
 * 
 */
package com.shangpin.iog.coach.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.coach.conf.ApiURL;
import com.shangpin.iog.coach.dto.Attributes;
import com.shangpin.iog.coach.dto.CoachError;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月4日
 */
public class CoachClient {
	static Logger logger =LoggerFactory.getLogger(CoachClient.class); 
	public static String token=null;
	public static long tokenExpire=0;
	public static long tokenCreate=0L; 
	public static int cnt=0;
	private static final ReentrantLock lock = new ReentrantLock();
	/**
	 * 
	 * @param page
	 * @param size
	 * @return
	 * @throws ServiceException
	 */
	public static String getAttribute(int page,int size) throws ServiceException{
		Map<String,String> param = getCommonParam(page,size);
		String url=paramGetUrl(ApiURL.ATTRIBUTES,param);
		String body=HttpUtils.get(url);
		Gson gson = new Gson();
		logger.info("response:\r\n"+body);
		Map<String,Attributes> attriMap=gson.fromJson(body, new TypeToken<Map<String,Attributes>>(){}.getType());
		logger.info("getAttribute result:\r\n"+gson.toJson(attriMap));
		return body;
	}
	/**
	 * get请求方式的url带上参数
	 * @param url 请求url
	 * @param param get参数
	 * @return
	 * @throws ServiceException 
	 */
	private static String paramGetUrl(String url, Map<String, String> param) throws ServiceException {
		Set<String> keys =param.keySet();
		StringBuffer sb = new StringBuffer(url);sb.append("?");
		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			sb.append(key).append("=").append(param.get(key)).append("&");
		}
		return sb.toString();
	}
	/**
	 * 获取公共参数
	 * @param page 可选页码
	 * @param size 可选页大小
	 * @return
	 * @throws ServiceException 获取token失败的异常
	 */
	private static Map<String, String> getCommonParam(int page, int size) throws ServiceException {
		Map<String,String> param = new HashMap<String, String>();
		param.put("page", page+"");param.put("limit", size+"");
		param.put("access_token", getToken());
		return param;
	}
	public static String getToken() throws ServiceException{
//测试
		if(token==null)
			return "6c9ade4c5fea79a5c0b060c67b55f4a2a59316dff3a18f047990484b8cc74d8c6ecddbbbb03139211f017ee9ea983f908ae5a46cf087294ccfdb46a78107fd01ea19a32e8f437e6cd38f3c1befce0fb2c0822fd2aa749cd5fbe2e7841b1590f7";
		lock.lock();
		if(token==null||System.currentTimeMillis()-tokenCreate>tokenExpire){
			token=initToken();
		}
		lock.unlock();
		return token;
	}
	
	/**
	 * {
	  "access_token": "abcd1234abcd1234abcd1234abcd1234",
	  "expires_in": 3600
	}
	 * @return 获取到的token
	 */
	private static String initToken() throws ServiceException{
		logger.info("初始化token......");
		try {
			token=null;
			String body=HttpUtils.post(ApiURL.AUTH, null, false, true, ApiURL.userName, ApiURL.password);
			logger.info("token:"+body);
			JsonElement je=toJsonElement(body);
			CoachError er=hasError(je);
			if(er!=null){
				throw new ServiceMessageException(er.getMessage());
			}
			JsonObject jo = je.getAsJsonObject();
			token=jo.get("access_token").getAsString();
			tokenExpire=jo.get("expires_in").getAsInt()*1000;
			tokenCreate=System.currentTimeMillis();
		}catch (Exception e) {
			logger.error("初始化token错误",e);
			throw new ServiceMessageException(e.getMessage());
		}
		return token;
	}
	
	public static CoachError hasError(JsonElement je){
		JsonElement msg=je.getAsJsonObject().get("message");
		if(msg!=null){
			Gson gson = new Gson();
			return gson.fromJson(je, CoachError.class);
		}
		return null;
	}
	/**
	 * @param body
	 * @return
	 */
	private static JsonElement toJsonElement(String body) {
		JsonParser jp =new JsonParser();
		JsonElement je=jp.parse(body);
		
		return je;
	}
	public static void main(String[] args) throws ServiceException, IOException {
		getAttribute(1, 100);
		System.in.read();
	}
}
