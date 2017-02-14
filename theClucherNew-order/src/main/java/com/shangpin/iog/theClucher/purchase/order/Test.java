package com.shangpin.iog.theClucher.purchase.order;

import java.io.File;
import java.io.FileWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConstants;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.fasterxml.jackson.annotation.ObjectIdGenerators.UUIDGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;



public class Test {

	public static void main(String[] args) {
		OrderDTO o = new OrderDTO();
		o.setDetail("52923-137:1");
		o.setSupplierOrderNo("2016120551035401111");
		o.setSpPurchaseNo("CGDF2016120556500");
		Gson gson = new Gson();
		 String json = gson.toJson(getOrder(o),PushOrderDTO.class);
//        json = "{\"orderNo\":\"CGD2016113000072\",\"orderItems\":[{\"skuID\":\"59197-112\",\"qty\":1}]}";
//        json="{\"app_key\":\"SHNGPN-001\",\"request\":{\"orderNo\":\"CGD2016113000072\",\"orderItems\":[{\"skuID\":\"59197-112\",\"qty\":1}]},\"time_stamp\":\"1480683566\",\"sign\":\"0A23C9E9C8A5D1DC9AB5A7C8914312CA\"}";
//		getJson(json);
	}
	 private static PushOrderDTO getOrder(OrderDTO orderDTO){
	    	PushOrderDTO order = new PushOrderDTO();
			String markPrice = null;
			List<ItemDTO> list = new ArrayList<ItemDTO>();
			try {
			     String detail = orderDTO.getDetail();
					int num = 0;
					String skuNo = null;
					num = Integer.parseInt(detail.split(":")[1]);
					skuNo = detail.split(":")[0];
					ItemDTO item = new ItemDTO();
					item.setSkuID(skuNo);
					item.setQty(num);
					list.add(item);
					order.setOrderItems(list);
					order.setOrderNo(orderDTO.getSupplierOrderNo());
					order.setPurchaseNo(orderDTO.getSpPurchaseNo());
			} catch (Exception e) {
				e.printStackTrace();
			}
	        return  order;
	    }
	public static String getJson(String p,String type,String uri) {
			String app_secret = "hM&L1dqGA5YGjGK%fU8*715D$g&z$B";
			Map<String, String> params = new LinkedHashMap<String, String>();
			String app_key = "SHNGPN-001";
			params.put("app_key", app_key);		
			params.put("request", p);
			String time = getUTCTime().getTime()+"";
			time = time.substring(0,10);
			params.put("time_stamp",time );
			String charset = "UTF-8";
			String md5_sign;
			String json = null;
			try {
				md5_sign ="app_key=" + app_key
					        + (params.containsKey("request") ? "&request=" + params.get("request") : "")
					               + "&time_stamp=" +params.get("time_stamp")
					                                             + "_" + app_secret;
				String md5_result = MD5.encrypt32(md5_sign);
				params.put("sign", md5_result);
				Map<String, String> headerMap = new LinkedHashMap<String, String>();
				headerMap.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
				String uuid = com.shangpin.iog.common.utils.UUIDGenerator.getUUID();
				String url = "http://ws.theclutcher.com/shangpin.svc/"+uri+"/"+uuid+"?app_key=SHNGPN-001&request="+URLEncoder.encode(p)+"&time_stamp="+time+"&sign="+md5_result;
				System.out.println("url:"+url);
				json = HttpUtil45.operateData(type, "json", url, new OutTimeConfig(1000*60*2,1000*60*2,1000*60*2), null, null, null, null);
				System.out.println(json);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return json;
		}
	
	   private static Date getUTCTime(){
		   final java.util.Calendar cal = java.util.Calendar.getInstance(); 
		   cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		    //2、取得时间偏移量：    
		    final int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);   
		    //3、取得夏令时差：    
		    final int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);    
		   
		    //4、从本地时间里扣除这些差量，即可以取得UTC时间：    
		    cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));    
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
	   
}
