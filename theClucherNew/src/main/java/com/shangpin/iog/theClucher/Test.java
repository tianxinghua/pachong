package com.shangpin.iog.theClucher;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;



public class Test {

	public static void main(String[] args) {
		System.out.println(getJson());
	}
	public static String getJson() {
			String app_secret = "hM&L1dqGA5YGjGK%fU8*715D$g&z$B";
			Map<String, String> params = new LinkedHashMap<String, String>();
			String app_key = "SHNGPN-001";
			params.put("app_key", app_key);		
			params.put("request", "");
			String time = getUTCTime().getTime()+"";
			time = time.substring(0,10);
			params.put("time_stamp",time );
			String charset = "UTF-8";
			String md5_sign;
			String json = null;
			try {
				  md5_sign = "app_key=" + ToolsUtils.urlEncode(app_key, charset)
					        + (params.containsKey("request") ? "&request=" + ToolsUtils.urlEncode(params.get("request"), charset) : "")
					               + "&time_stamp=" +ToolsUtils.urlEncode( params.get("time_stamp"),charset)
					                                             + "_" + app_secret;
				String md5_result = MD5.encrypt32(md5_sign);
				params.put("sign", md5_result);
				Map<String, String> headerMap = new LinkedHashMap<String, String>();
				headerMap.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
				System.out.println("params = " + params.toString());
				String url = "http://ws.theclutcher.com/shangpin.svc/products/36138-88";
				json = HttpUtil45.operateData("get", null, url,  new OutTimeConfig(1000*60*20,1000*60*20,1000*60*20), params, null, headerMap, null, null);
				System.out.println("result " + json);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return json;
		}
	   private static Date getUTCTime(){
		   final java.util.Calendar cal = java.util.Calendar.getInstance(); 
		   cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		    System.out.println(cal.getTime());  
		    //2、取得时间偏移量：    
		    final int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);   
		    System.out.println(zoneOffset);  
		    //3、取得夏令时差：    
		    final int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);    
		   
		    System.out.println(dstOffset);  
		    //4、从本地时间里扣除这些差量，即可以取得UTC时间：    
		    cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));    
	        System.out.println(cal.getTime());
	        return cal.getTime();
	    }
	   public static long GetTicks() 
	   { 
	     //convert the target-epoch time to a well-format string 
//		   Calendar cal = Calendar.getInstance() ;  
//	        // 2、取得时间偏移量：  
//	        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);  
//	        // 3、取得夏令时差：  
//	        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);  
//	        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：  
//	        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));  
		  SimpleDateFormat s = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss"); 
	      String date =s.format(getUTCTime()); 
	      String[] ds=date.split("/"); 
	         
	      //start of the ticks time 
	     Calendar calStart=Calendar.getInstance(); 
	     calStart.set(1, 1, 3, 0, 0, 0); 
	      
	     //the target time 
	     Calendar calEnd=Calendar.getInstance(); 
	     calEnd.set(Integer.parseInt(ds[0]) ,Integer.parseInt(ds[1]),Integer.parseInt(ds[2]),Integer.parseInt(ds[3]),Integer.parseInt(ds[4]),Integer.parseInt(ds[5]) ); 
	      
	     //epoch time of the ticks-start time 
	     long epochStart=calStart.getTime().getTime(); 
	     //epoch time of the target time 
	     long epochEnd=calEnd.getTime().getTime(); 
	      
	     //get the sum of epoch time, from the target time to the ticks-start time 
	      long all=epochEnd-epochStart;    
	      //convert epoch time to ticks time 
	         long ticks=( (all/1000) * 1000000) * 10; 
	         
	         return ticks; 
	   }
	   
	   
	   
	   
	   //字符串保存到本地文件中
	   private static void readLine(String content){
	     	File file = new File("C://clutcher.json");
	     	FileWriter fwriter = null;
	     	   try {
	     	    fwriter = new FileWriter(file);
	     	    fwriter.write(content);
	     	   } catch (Exception ex) {
	     	    ex.printStackTrace();
	     	   } finally {
	     	    try {
	     	     fwriter.flush();
	     	     fwriter.close();
	     	    } catch (Exception ex) {
	     	     ex.printStackTrace();
	     	    }
	     	   }
	     }
	   
	   
	   
	   
}