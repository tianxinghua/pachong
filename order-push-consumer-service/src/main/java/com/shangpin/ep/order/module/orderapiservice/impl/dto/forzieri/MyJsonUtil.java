package com.shangpin.ep.order.module.orderapiservice.impl.dto.forzieri;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;

/**
 * Created by Administrator on 2015/9/21.
 */
public class MyJsonUtil {
	
	private static ResourceBundle bdl = null;
	private static String accessTokenUrl = "https://api.forzieri.com/test/oauth/token";
	private static String refreshTokenUrl = "https://api.forzieri.com/test/oauth/token";
	private static String requestSourceUrl = null;
	private static String client_id = "NTkyZThjNGI2NGY2NTY1";
	private static String client_secret = "3db7c25746f4a11bd8f877b1132dc4a5e739535e";
	private static String username = "shangpin";
	private static String password = "AAWMGirAD9qxG@xuWsTmZKCbfNXJN6A,";
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
	
		map.put("username", username);
		map.put("password", password);
		auth(map);
	}
	
	/*
	 * 厂家授权
	 */
	public static String auth(Map<String, String> map) {

		map.put("grant_type", "password");
		map.put("client_id", client_id);
		map.put("client_secret", client_secret);
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
	 * 厂家授权
	 */
	public static String getAccessTokenJson(Map<String, String> map) {

		map.put("grant_type", "authorization_code");
		map.put("client_id", client_id);
		map.put("client_secret", client_secret);
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
		map.put("client_id", client_id);
		map.put("client_secret", client_secret);
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