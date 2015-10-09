package com.shangpin.iog.reebonz.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.reebonz.dto.Order;
import com.shangpin.iog.reebonz.dto.RequestObject;

/**
 * Created by Administrator on 2015/9/21.
 */
public class MyJsonUtil {

	/*
	 * 下订单锁库存
	 */
	public static String lockStock(String order_id, String order_site,
			String data) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("order_id", order_id);
		map.put("order_site", order_site);
		map.put("data", data);
		String json = null;
		try {
			json = HttpUtil45
					.post("http://ladon.sit.titan.reebonz-dev.com/api/eps_product_reservation",
							map, new OutTimeConfig(1000 * 600, 1000 * 60,
									1000 * 600));

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("锁库存的返回结果：" + json);
		return json;
	}

	/*
	 * 解开锁库存
	 */
	public static String unlockStock(String reservation_id, String order_id,
			String user_id, String confirmation_code) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("reservation_id", reservation_id);
		map.put("order_id", order_id);
		map.put("user_id", user_id);
		map.put("confirmation_code", confirmation_code);
		String json = null;
		try {
			json = HttpUtil45
					.post("http://ladon.sit.titan.reebonz-dev.com/api/eps_product_reservation_confirmation",
							map, new OutTimeConfig(1000 * 600, 1000 * 60,
									1000 * 600));

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("解库存的返回结果：" + json);
		return json;
	}

	/*
	 * 推送订单
	 */
	public static String pushOrder(Map<String, String> map) {

		String json = null;
		try {
			json = HttpUtil45.post("",
					map, new OutTimeConfig(1000 * 600, 1000 * 60, 1000 * 600));

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("下订单的返回结果：" + json);
		return json;
	}

	/*
	 * 厂家授权
	 */
	public static String getAccessTokenJson(Map<String, String> map) {

		map.put("grant_type", "password");
		String json = null;
		try {
			json = HttpUtil45
					.post("http://oauth.uat.titan.reebonz-dev.com/api/retrieve_token.json",
							map, new OutTimeConfig(1000 * 20, 1000 * 20,
									1000 * 20));
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
					.post("http://oauth.uat.titan.reebonz-dev.com/api/refresh_token.json",
							map, new OutTimeConfig(1000 * 20, 1000 * 20,
									1000 * 20));
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
					.post("http://oauth.uat.titan.reebonz-dev.com/api/request_resource.json",
							map, new OutTimeConfig(1000 * 20, 1000 * 20,
									1000 * 20));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("通过授权请求url的返回结果：" + json);
		return json;
	}
}
