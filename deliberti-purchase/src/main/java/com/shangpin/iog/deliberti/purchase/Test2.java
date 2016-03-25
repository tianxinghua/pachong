package com.shangpin.iog.deliberti.purchase;

import java.util.HashMap;
import java.util.Map;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

public class Test2 {
	private static  String url = "http://www.gicos.it/gettest.php";
	private static  OutTimeConfig defaultConfig = new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10);
	static String rtnData = null;
	
	public static void testOrder(){
		Map<String, String> map =new HashMap<String, String>();
		map.put("1", "13618;13618;shangpin;shangpin;wangsaying@shangpin.com;S;;0;123123;VIA CUPA;NAPOLI;80131;NA;3348053248;Italy;IT;2;10/02/2015|");
		map.put("2", ";manuela fasanoVIA CUPA CAMAOLDOLI 10|");
		map.put("3", "1;235850;38;79|");
		map.put("4", "Totale Prodotti (iva inclusa):;79|");
		map.put("9", "3400988|");
		rtnData = HttpUtil45.post(url, map, defaultConfig);
		System.out.println("rtnData="+rtnData);
	}
	
	public static void testOrder1(){
		String Url = "1;13618;13618;shangpin;shangpin;wangsaying@shangpin.com;S;;0;123123;VIA CUPA;NAPOLI;80131;NA;3348053248;"
				+ "Italy;IT;2;10/02/2015|2;manuela fasanoVIA CUPA CAMAOLDOLI 10|3;1;235850;38;79|3;1;218894;38;88|4;"
				+ "otale Prodotti (iva inclusa):;79|4;Corriere Espresso SDA/DHL:;0|4;Contributo per contrassegno:;7|4;Totale Ordine (iva inclusa):;86|9;3400988|";
		Map<String, String> map =new HashMap<String, String>();
		map.put("key", Url);
		rtnData = HttpUtil45.post(url,map, defaultConfig);
		System.out.println("rtnData="+rtnData);
	}
	
	public static void main(String[] args) {
		testOrder1();
	}

}
