package com.shangpin.iog.monti.test;

import java.util.HashMap;
import java.util.Map;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

public class SetOrderTest {
	private static  String url = "http://net13server2.net/MontiApi/Myapi/Productslist/setOrder";
	private static  OutTimeConfig defaultConfig = new OutTimeConfig(1000 * 2, 1000 * 60, 1000 * 60);
	static String rtnData = null;
	
	public static void pushOrder(){
		Map<String, String> map =new HashMap<String, String>();
		map.put("DBContext", "Default");
		map.put("purchase_no", "CGD2015120300019");
		map.put("order_no", "201512030034056");
		map.put("barcode", "2004665800032");
		map.put("ordQty", "1");
		map.put("sellPrice", "200");
		map.put("key", "jAXO6D34vh");
		rtnData = HttpUtil45.get(url, defaultConfig , map);
		System.out.println("推送订单返回结果为="+rtnData);
	}
	
	public static void main(String[] args) {
		pushOrder();
		
	}

}
