package com.shangpin.api.airshop.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;


public class HttpPostUploadUtil {
	public static void main(String[] args) {
		String filepath = "C://C.txt";
		String urlStr = "http://127.0.0.1:8080/print/printFile";
		Map<String,String> map = new HashMap<String,String>();
		map.put("content", file2String(new File(filepath)));
	}
	 public static String file2String(File file) { 
         StringBuffer sb = new StringBuffer(); 
         try { 
                 LineNumberReader reader = new LineNumberReader(new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))); 
                 String line; 
                 while ((line = reader.readLine()) != null) { 
                         sb.append(line).append(System.getProperty("line.separator")); 
                 } 
         } catch (Exception e) { 
         } 
         return sb.toString(); 
 } 
}