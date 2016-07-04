package com.shangpin.iog.generater.util.publicJ;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class DowLoad {

	/**
	 * http下载txtcsv文件到本地路径
	 * 
	 * @throws MalformedURLException
	 */
	public static void txtDownload(String url,String filepath){
		String csvFile = getHttpStr(url);
		if (csvFile.contains("发生错误异常")) {
			try {
				Thread.currentThread().sleep(5000l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			csvFile = getHttpStr(url);
		}
		FileWriter fwriter = null;
		try {
			File file = new File(filepath);
			if (!file.exists()) {
				file.createNewFile();
			}
			fwriter = new FileWriter(filepath);
			fwriter.write(csvFile);
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
	
	private static String getHttpStr(String url) {
		
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = httpClientBuilder.build();
//		HttpPost httpPost = new HttpPost("https://api.forzieri.com/test/orders");
		HttpGet httpPost = new HttpGet(url);
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        CloseableHttpResponse response = null;
        String str = "";
		try {
			response = httpClient.execute(httpPost);
			str = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
//		String str = HttpUtil45.get(url, new OutTimeConfig(1000 * 60 * 10,1000 * 60 * 20, 1000 * 60 * 20), null);
		return str;
	}
}
