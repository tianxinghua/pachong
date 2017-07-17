package com.shangpin.ep.order.module.orderapiservice.impl.dto.forzieri;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;

/**
 * Created by Administrator on 2015/9/21.
 */
public class MyJsonUtil {
	
	private static OAuth oAtuth = null;
	private static ResourceBundle bdl = null;
	private static String accessToken = "b8abbb17240e6b95ed2552bea4bdcb856d58e615";
	private static String refreshToken = "d347b88bf9d7fa3ccaf0dfe9df01d23056d16fa4";
	private static String refreshTokenUrl = "https://api.forzieri.com/test/oauth/token";
	private static String requestSourceUrl = "";
	private static String client_id = "NTkyZThjNGI2NGY2NTY1";
	private static String client_secret = "3db7c25746f4a11bd8f877b1132dc4a5e739535e";
	private static String username = "shangpin";
	private static String password = "AAWMGirAD9qxG@xuWsTmZKCbfNXJN6A,";
//	static {
//		if (null == bdl) {
//			bdl = ResourceBundle.getBundle("conf");
//		}
//		accessToken = bdl.getString("accessToken");
//		refreshToken = bdl.getString("refreshToken");
//		refreshTokenUrl = bdl.getString("refreshTokenUrl");
//		requestSourceUrl = bdl.getString("requestSourceUrl");
//		
//		oAtuth = new OAuth();
//		oAtuth.setAccess_token(accessToken);
//		oAtuth.setRefresh_token(refreshToken);
//		
//	}

	public static void main(String[] args) {
		String placeOrderUrl = "https://api.forzieri.com/v3/products/az30277-002-00";
		Map headerMap = new HashMap();
    	headerMap.put("Authorization","Bearer b8abbb17240e6b95ed2552bea4bdcb856d58e615");
		String ss = HttpUtil45.get(placeOrderUrl, null,null, headerMap,null,null);
		System.out.println(ss);
	}
	public static OAuth getOAuth(){
		return oAtuth;
	}
	
	/*
	 * 刷新授权
	 * */
	public static String refreshTokenJson() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("grant_type", "refresh_token");
		map.put("client_id", client_id);
		map.put("client_secret", client_secret);
		map.put("refresh_token", refreshToken);
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