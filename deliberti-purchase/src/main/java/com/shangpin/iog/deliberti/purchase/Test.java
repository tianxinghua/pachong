package com.shangpin.iog.deliberti.purchase;

import java.util.HashMap;
import java.util.Map;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

public class Test {
	private static  String url = "http://www.gicos.it/gettest.php";
	private static  OutTimeConfig defaultConfig = new OutTimeConfig(1000*10*60, 1000*60*10, 1000*60*10);
	static String rtnData = null;
	
	public static void main(String[] args) {
		pushOrder();
	}
	
	public static void pushOrder(){

		String jsonValue = "1;13618;13618;shangpin;shangpin;wangsaying@shangpin.com;S;;0;123123;VIA CUPA;NAPOLI;80131;NA;3348053248;Italy;IT;2;10/02/2015|2;manuela fasanoVIA CUPA CAMAOLDOLI 10|3;1;235850;38;79|3;1;218894;38;88|4;Totale Prodotti (iva inclusa):;79|4;Corriere Espresso SDA/DHL:;0|4;Contributo per contrassegno:;0|4;Totale Ordine (iva inclusa):;86|9;3400988|";
		
		try {
			rtnData = HttpUtil45.operateData("post", "json", url, defaultConfig, null, jsonValue, "", "");

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("rtnData="+rtnData);
	}

}
