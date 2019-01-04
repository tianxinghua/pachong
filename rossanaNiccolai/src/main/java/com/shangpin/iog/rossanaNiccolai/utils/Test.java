package com.shangpin.iog.rossanaNiccolai.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.xml.bind.JAXBException;

import com.google.gson.Gson;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.rossanaNiccolai.dao.Result;
import com.shangpin.iog.rossanaNiccolai.dao.ReturnObject;
public class Test {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
//		System.out.println(getUTCTime());
		String json = getJson("");
		@SuppressWarnings("rawtypes")
		Map map = new HashMap();
		map.put("storeCode","DW3LT");
		map.put("order",json);
//		map.put("format","json");
		try {
			String rtnData = HttpUtil45.post("http://api.gebnegozi.com/api/v2/place/order.json",map,new OutTimeConfig(1000 * 60, 1000 * 60,
					1000 * 60));
			String s = HttpUtil45.get("http://demo-efashion.edstema.it/api/v3.0/products.json?storeCode=DW3LT", new OutTimeConfig(1000 * 60, 1000 * 60,
					1000 * 60), null);
//			String rtnData = HttpUtil45.operateData("post", "json", "http://demo-efashion.edstema.it/api/v3.0/place/order.json", null, map, json, "", "");
			System.out.println(rtnData);	
			System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	  private static String getUTCTime(){
	    	// 1、取得本地时间：  
	        Calendar cal = Calendar.getInstance() ;  
	        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss" );
	        // 2、取得时间偏移量：  
	        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);  
	        // 3、取得夏令时差：  ;
	        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);  
	        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：  
	        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));  
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss" );
	        return sdf.format(cal.getTime());
	    }
	  private static String getJson(String fileName) {

			String fullFileName = "C:/test.json" ;

			File file = new File(fullFileName);
			Scanner scanner = null;
			StringBuilder buffer = new StringBuilder();
			try {
				scanner = new Scanner(file, "utf-8");
				while (scanner.hasNextLine()) {
					buffer.append(scanner.nextLine());
				}
			} catch (Exception e) {

			} finally {
				if (scanner != null) {
					scanner.close();
				}
			}
			System.out.println(buffer.toString());
			return buffer.toString();
		}
}
