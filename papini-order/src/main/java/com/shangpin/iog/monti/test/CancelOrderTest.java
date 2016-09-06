package com.shangpin.iog.monti.test;

import java.util.HashMap;
import java.util.Map;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ReturnOrderDTO;

public class CancelOrderTest {
	private static  String url = "http://80.88.89.209/papiniapi/Myapi/Productslist/CancelOrder";
	private static  OutTimeConfig defaultConfig = new OutTimeConfig(1000 * 2, 1000 * 60, 1000 * 60);
	static String rtnData = null;
	
	private static void cancelOrder(String status){
		Map<String, String> map =new HashMap<String, String>();
		map.put("DBContext", "Default");
		map.put("purchase_no", "CGD2016090300019");
		map.put("order_no", "201609030034056");
		map.put("key", "doV3IeZ6bz");
		rtnData = HttpUtil45.get(url, defaultConfig , map);
		System.out.println("取消成功");
		System.out.println("取消订单返回结果="+ rtnData);
	}
	
	
	
	public static void main(String[] args) {
		//ReturnOrderDTO deleteOrder;
		cancelOrder("payed");
	}
	
	
	
	
	

}
