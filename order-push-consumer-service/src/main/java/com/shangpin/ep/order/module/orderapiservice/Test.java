package com.shangpin.ep.order.module.orderapiservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.alibaba.fastjson.JSONObject;

public class Test {
	private static final int TIMEOUT = 2*1000;
	public static void main(String[] args) throws Exception{
		  	URL url = new URL("http://www.luxury888.it/holdorders/e3eb4b7dd1bc4d33bfe54a095485b6b9");  
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();  
	        connection.setRequestMethod("DELETE");  
	        connection.setDoInput(true);  
	        connection.setDoOutput(true);  
	        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
	        connection.setConnectTimeout(TIMEOUT);
	        connection.setRequestProperty("content-type", "text/plain; charset=UTF-8");  
	        OutputStreamWriter out = new OutputStreamWriter(  
	                connection.getOutputStream(), "UTF-8");  
	        // 将要传递的集合转换成JSON格式  
	        JSONObject js = new JSONObject();
	        js.put("order_no","201703231100111");
	        js.put("status","free");
	        // 组织要传递的参数  
	        out.write("" + js);  
	        out.flush();  
	        out.close();  
	        // 获取返回的数据  
	        BufferedReader in = new BufferedReader(new InputStreamReader(  
	                connection.getInputStream()));  
	        String line = null;  
	        StringBuffer content = new StringBuffer();  
	        while ((line = in.readLine()) != null) {  
	            // line 为返回值，这就可以判断是否成功  
	            content.append(line);  
	        }  
	        in.close();  
	        System.out.println(content.toString());
	}
}
