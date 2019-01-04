package com.shangpin.api.airshop.util;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.methods.HttpPost;

public class SendXML_HK {
	public static void main(String[] args){
		try{
			
			String boundary = "Boundary-b1ed-4060-99b9-fca7ff59c113"; //Could be any string
			String Enter = "\r\n";
			
			File xml = new File("C:\\C.txt");
			FileInputStream fis = new FileInputStream(xml);
			
			URL url = new URL("http://127.0.0.1:8080/stock/down");
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setUseCaches(false);
			 conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setInstanceFollowRedirects(true);
//			conn.setRequestProperty("Content-Type","multipart/form-data;"); 
//	       conn.setRequestProperty("User-Agent",
//	             "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
//	        conn.setRequestProperty("Content-Type",
//	        "multipart/form-data; boundary=" + "--------------------");
			conn.connect();
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			byte[] xmlBytes = new byte[fis.available()];
			fis.read(xmlBytes);
			
			dos.write(xmlBytes);
			
			dos.flush();
			dos.close();
			fis.close();
			System.out.println("status code: "+conn.getResponseCode());
			conn.disconnect();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
