package com.shangpin.iog.theClucher;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;



public class Test {

	public static void main(String[] args) {
			String app_secret = "hM&L1dqGA5YGjGK%fU8*715D$g&z$B";
			Map<String, String> params = new LinkedHashMap<String, String>();
			String app_key = "SHNGPN-001";
			params.put("app_key", app_key);		
			
			params.put("request", "");
			String time = getUTCTime().getTime()+"";
			time = time.substring(0,10);
			System.out.println(time);
			params.put("time_stamp",time );
			String charset = "UTF-8";
			String md5_sign;
			try {
				md5_sign = "app_key=" + ToolsUtils.urlEncode(app_key, charset)
				        + (params.containsKey("request") ? "&request=" + ToolsUtils.urlEncode(params.get("request"), charset) : "")
				        + "&time_stamp=" + ToolsUtils.urlEncode(params.get("time_stamp"), charset)
				        + "_" + app_secret;
				System.out.println("before  md5  = " + md5_sign);
				String md5_result = MD5.encrypt32(ToolsUtils.urlEncode(md5_sign, charset));
				System.out.println("md5_result = " + md5_result);
				params.put("sign", md5_result);
				
				Map<String, String> headerMap = new LinkedHashMap<String, String>();
				headerMap.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
				System.out.println("params = " + params.toString());
				String url = "http://ws.theclutcher.com/shangpin.svc/products";
				String json = HttpUtil45.operateData("get", "json", url,  new OutTimeConfig(), params, null, headerMap, null, null);
//				String result =HttpUtil45.post(url, params, headerMap, new OutTimeConfig());
//				String result = HttpHelper.PostForm(host + url, headerMap, params);
				System.out.println("result " + json);
			} catch (Exception e) {
				e.printStackTrace();
			}
	
		}
	   private static Date getUTCTime(){
	    	// 1、取得本地时间：  
	        Calendar cal = Calendar.getInstance() ;  
	        // 2、取得时间偏移量：  
	        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);  
	        // 3、取得夏令时差：  
	        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);  
	        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：  
	        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));  
	        
	        return cal.getTime();
	    }
}
