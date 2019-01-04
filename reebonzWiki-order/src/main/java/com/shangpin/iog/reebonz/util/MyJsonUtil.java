package com.shangpin.iog.reebonz.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

/**
 * Created by Administrator on 2015/9/21.
 */
public class MyJsonUtil {
	
	private static ResourceBundle bdl = null;
	private static String accessTokenUrl = "http://api.reebonz.com/api/retrieve_token.json";
	private static String refreshTokenUrl = null;
	private static String requestSourceUrl = null;
//	static {
//		if (null == bdl) {
//			bdl = ResourceBundle.getBundle("conf");
//		}
//		accessTokenUrl = bdl.getString("accessTokenUrl");
//		refreshTokenUrl = bdl.getString("refreshTokenUrl");
//		requestSourceUrl = bdl.getString("requestSourceUrl");
//	}
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("client_id", "wXWzjVkdW2hpu2FkIIIXmA==");
		map.put("client_secret", "Nxd66x17eorW7HioXbrI9gDKs+LBKf0erOha6m/TZCq8mQZSqRNT+c2MllLSALAGDQRBrDuk2Xm3xc1KBVr+qg==");
		map.put("username", "shangpin");
		map.put("password", "y9LZhbx6VhfKzu/FCY3bHw==");
		getAccessTokenJson(map);
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
