package com.shangpin.iog.reebonz.util;

import java.util.HashMap;
import java.util.Map;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

/**
 * Created by Administrator on 2015/9/21.
 */
public class MyJsonUtil {
	/**
	 * 第一步：获取活动信息
	 * */
	public static String getReebonzEventJson(String eventUrl) {
		String json = null;
		try {
			json = HttpUtil45
					.get(eventUrl,
							new OutTimeConfig(1000 * 20, 1000 * 20, 1000 * 20),
							null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("抓取的活动列表："+json);
		return json;
	}

	/**
	 * 第二步：根据活动获取商品信息
	 * */
	public static String getReebonzSpuJsonByEventId(String productUrl,String eventId,int start,int rows) {
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("event_id", eventId);
		map.put("start",String.valueOf(start));
		map.put("rows", String.valueOf(rows));
		String json = null;
		try {
			json = HttpUtil45
					.get(productUrl,
							new OutTimeConfig(1000 * 60, 1000 * 120, 1000 * 60),
							map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("抓取的活动"+eventId+"的商品列表："+json);
		if("{\"error\":\"发生异常错误\"}".equals(json)){
			json = null;
		}
		return json;
	}

	/**
	 * 第三步：根据活动Id和skuId获取库存以及尺码
	 * */
	public static String getSkuScokeJson(String stockUrl,String eventId, String skuId) {

		Map<String,String> map = new HashMap<String,String>();
		map.put("event_id", eventId);
		map.put("sku", skuId);
		String json = null;
		try {
			json = HttpUtil45
					.get(stockUrl,
							new OutTimeConfig(1000 * 120, 1000 * 120, 1000 * 120),
							map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("--抓取的活动:"+eventId+"以及skuId:"+skuId+"的商品库存："+json);
		return json;
	}

	/**
	 *  获取参加某一活动的商品总数
	 * */
	public static String getProductNum(String productUrl,String eventId) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("event_id", eventId);
		map.put("start","0");
		map.put("rows", "0");
		String json = null;
		try {
			json = HttpUtil45
					.get(productUrl,
							new OutTimeConfig(1000 * 600, 1000 * 60, 1000 * 600),
							map);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("抓取的活动"+eventId+"的商品总数："+json);
		return json;
	}

	/*
	 * 下订单锁库存
	 * */
	public static String lockStock(String order_id, String order_site, String data) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("order_id", order_id);
		map.put("order_site",order_site);
		map.put("data", data);
		String json = null;
		try {
			json = HttpUtil45
					.post("http://ladon.sit.titan.reebonz-dev.com",map,
							new OutTimeConfig(1000 * 600,1000 * 60, 1000 * 600));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("锁库存的返回结果："+json);
		return json;
	}

	/*
	 * 解开锁库存
	 * */
	public static String unlockStock(String reservation_id, String order_id,
			String user_id, String confirmation_code) {
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("reservation_id", reservation_id);
		map.put("order_id", order_id);
		map.put("user_id",user_id);
		map.put("confirmation_code", confirmation_code);
		String json = null;
		try {
			json = HttpUtil45
					.post("http://ladon.sit.titan.reebonz-dev.com",map,
							new OutTimeConfig(1000 * 600,1000 * 60, 1000 * 600));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("锁库存的返回结果："+json);
		return json;
	}
	
}
