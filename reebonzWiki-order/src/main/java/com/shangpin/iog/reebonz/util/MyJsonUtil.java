package com.shangpin.iog.reebonz.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONArray;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.reebonz.dto.OAuth;
import com.shangpin.iog.reebonz.dto.Order;
import com.shangpin.iog.reebonz.dto.RequestObject;

/**
 * Created by Administrator on 2015/9/21.
 */
public class MyJsonUtil {
	
	private static ResourceBundle bdl = null;
	private static String accessTokenUrl = null;
	private static String refreshTokenUrl = null;
	private static String requestSourceUrl = null;
	static {
		if (null == bdl) {
			bdl = ResourceBundle.getBundle("conf");
		}
		accessTokenUrl = bdl.getString("accessTokenUrl");
		refreshTokenUrl = bdl.getString("refreshTokenUrl");
		requestSourceUrl = bdl.getString("requestSourceUrl");
	}
	/*
	 * 厂家授权
	 */
	public static String getAccessTokenJson(Map<String, String> map) {

		map.put("grant_type", "password");
		String json = null;
		try {
			json = HttpUtil45
					.post(accessTokenUrl,
							map, new OutTimeConfig(1000 * 60, 1000 * 60,
									1000 * 60));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("授权返回的结果：" + json);
		return json;
	}

	/*
	 * 刷新授权
	 * */
	public static String refreshTokenJson(Map<String, String> map) {
		map.put("grant_type", "refresh_token");
		String json = null;
		try {
			json = HttpUtil45
					.post(refreshTokenUrl,
							map, new OutTimeConfig(1000 * 60, 1000 * 60,
									1000 * 60));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("刷新授权返回的结果：" + json);
		return json;
	}
	
	/*
	 * 请求ReebonzAPI
	 * */
	public static String getRequestSourceJson(Map<String, String> map) {
		
		String json = null;
		try {
			json = HttpUtil45
					.post(requestSourceUrl,
							map, new OutTimeConfig(1000 * 60*5, 1000 * 60*5,
									1000 * 60*5));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("通过授权请求url的返回结果：" + json);
		return json;
	}
}
