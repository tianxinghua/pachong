package com.shangpin.iog.spinnaker;

import java.util.HashMap;
import java.util.Map;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

public class SetOrderTest {
	private static  String url = "http://185.58.119.177/spinnakerapi/Myapi/Productslist/setOrder";
	private static  OutTimeConfig defaultConfig = new OutTimeConfig(1000 * 2, 1000 * 60, 1000 * 60);
	static String rtnData = null;
	
	public static void pushOrder(){
		Map<String, String> map =new HashMap<String, String>();
		map.put("DBContext", "Default");
		map.put("purchase_no", "CGD2015120300017");
		map.put("order_no", "201512030034054");
		map.put("barcode", "2004238900042");
		map.put("ordQty", "1");
		map.put("sellPrice", "200");
		map.put("key", "8IZk2x5tVN");
		rtnData = HttpUtil45.get(url, defaultConfig , map);
		System.out.println("推送订单返回结果为="+rtnData);
	}
	
	public static void main(String[] args) {
		pushOrder();
		
	}

}
