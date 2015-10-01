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
	public static String getReebonzEventJson() {
		String json = null;
		try {
			json = HttpUtil45
					.get("http://hps.sit.titan.reebonz-dev.com/api/shangpin/event_list?start=2&rows=1",
							new OutTimeConfig(1000 * 20, 1000 * 20, 1000 * 20),
							null);

			if (json.equals("{\"error\":\"发生异常错误\"}")) {
				System.out.println("连接超时");
				throw  new  ServiceMessageException("1");
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("抓取的活动列表："+json);
		return json;
	}

	/**
	 * get tony data
	 * */
	public static String getReebonzSpuJsonByEventId(String eventId,int num) {
		
		


		Map<String,String> map = new HashMap<String,String>();
		map.put("event_id", eventId);
//		map.put("start",String.valueOf(num));
//		map.put("rows", "3");
		String json = null;
		try {
			json = HttpUtil45
					.get("http://hps.sit.titan.reebonz-dev.com/api/shangpin/product_list",
							new OutTimeConfig(1000 * 60, 1000 * 120, 1000 * 60),
							map);
//			if (json.equals("{\"error\":\"发生异常错误\"}")) {
//				// 重复调用5次
//				int i=0;
//				while(true){
//					json = HttpUtil45
//							.get("http://hps.sit.titan.reebonz-dev.com/api/shangpin/product_list",
//									new OutTimeConfig(1000 * 60, 1000 * 60, 1000 * 60),
//									map);
//					if (json.equals("{\"error\":\"发生异常错误\"}")){
//						i++;
//					}else{
//						continue;
//					}
//					if(i==5){
//						break;
//					}
//				}
//			
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("抓取的活动"+eventId+"的商品列表："+json);
		return json;
	}

	/**
	 * get tony data
	 * */
	public static String getSkuScokeJson(String eventId, String skuId) {

		Map<String,String> map = new HashMap<String,String>();
		map.put("event_id", eventId);
		map.put("sku", skuId);
		String json = null;
		try {
			json = HttpUtil45
					.get("http://hps.sit.titan.reebonz-dev.com/api/shangpin/product_qty",
							new OutTimeConfig(1000 * 60, 1000 * 60, 1000 * 60),
							map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("--抓取的活动:"+eventId+"以及skuId:"+skuId+"的商品库存："+json);
		return json;
	}

	public static void main(String[] args) {
	}

	public static String getProductNum(String eventId) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("event_id", eventId);
		map.put("start","0");
		map.put("rows", "0");
		String json = null;
		try {
			json = HttpUtil45
					.get("http://hps.sit.titan.reebonz-dev.com/api/shangpin/product_list",
							new OutTimeConfig(1000 * 600, 1000 * 60, 1000 * 600),
							map);
			
		} catch (Exception e) {
			json = null;
			System.out.println("连接超时");
			e.printStackTrace();
		}
		System.out.println("抓取的活动"+eventId+"的商品列表："+json);
		return json;
	}
}
