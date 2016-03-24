package com.shangpin.iog.meifeng.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

public class sss {
	public static void main(String[] args) {
  	Map<String, String> param = new HashMap<String,String>();
    	
        System.out.println("开始获取数据");
//        String post = HttpUtil45.postAuth("http://79.62.242.6:8088/ws_sito_p15/ws_sito_p15.asmx/GetAllAvailabilityMarketplace", null, new OutTimeConfig(1000*60*120,1000*60*120,1000*60*120), "shangpin", "creative99");
        String post = HttpUtil45.post("http://79.62.242.6:8088/ws_sito_p15/ws_sito_p15.asmx/GetAllAvailabilityMarketplace", null, new OutTimeConfig(1000*60*120,1000*60*120,1000*60*120));
		System.out.println("开始保存数据");
		File file = new File("E://daaaaaaaaaa.txt");
		if (!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
				
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		FileWriter fwriter = null;
		try {
			fwriter = new FileWriter("E://daaaaaaaaaa.txt");
			fwriter.write(post);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				fwriter.flush();
				fwriter.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
        
	}
}
